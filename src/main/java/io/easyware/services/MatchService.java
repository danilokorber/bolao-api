package io.easyware.services;

import io.easyware.entities.Bet;
import io.easyware.entities.Match;
import io.easyware.repositories.MatchRepository;
import io.easyware.shared.keycloak.KeycloakService;
import lombok.extern.java.Log;
import org.jose4j.json.internal.json_simple.JSONObject;
import org.jose4j.json.internal.json_simple.parser.JSONParser;
import org.jose4j.json.internal.json_simple.parser.ParseException;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@ApplicationScoped
@Log
public class MatchService {
    private final String TOKEN = "1a8ebe538b1d4c71a2ea2e30e518236e";

    private final MatchRepository matchRepository;

    @Context
    SecurityContext sec;

    @Inject
    KeycloakService keycloakService;

    public MatchService(MatchRepository matchRepository) {
        this.matchRepository = matchRepository;
    }

    public List<Match> findAll() {
        List<Match> matches = matchRepository.findAll().list();
        matches.forEach(this::calculatePoints);
        return matches;
    }

    public Match find(long matchId) {
        Match found = matchRepository.findById(matchId);
        calculatePoints(found);
        return found;
    }

    public Optional<Match> findKOMatchWithHomeId(long koMatchId) {
        return matchRepository.findAll().stream().filter(m -> m.getHome().getId() == koMatchId).findFirst();
    }
    public Optional<Match> findKOMatchWithAwayId(long koMatchId) {
        return matchRepository.findAll().stream().filter(m -> m.getAway().getId() == koMatchId).findFirst();
    }

    public void calculatePoints(Match match) {
        if (match != null) {
            List<Bet> bets = match.getBets().stream().filter(bet -> bet.isActive()).collect(Collectors.toList());
            if (match.getKickoff().before(new Date())) {
                // Match has started. Calculating points..
                bets.forEach(bet -> bet.setPoints(PointsService.calculatePoints(match, bet)));
            }
            match.setBets(bets);
        }
    }

    public void keepOnlyBetsOf(List<Match> matches, String userId) {
        matches.forEach(m -> keepOnlyBetsOf(m, userId));
    }

    public void keepOnlyBetsOf(Match match, String userId) {
        List<Bet> filteredBets = match.getBets().stream().filter(bet -> bet.isActive() && bet.getUserId().equals(userId)).collect(Collectors.toList());
        match.setBets(filteredBets);
    }

    public boolean matchIsFromTournamentEditions(long matchId, long tournamentEditionId) {
        Match match = find(matchId);
        return match.getTournamentEditionId() == tournamentEditionId;
    }

    public List<Match> liveMatches() {
        return matchRepository.listAll().stream().filter(Match::isLive).collect(Collectors.toList());
    }

    public void sortByKickOff(List<Match> matches) {
        matches.sort(Comparator.comparing(Match::getKickoff));
    }

    @Transactional
    public void updateResult(Match match) throws IOException, ParseException {
        URL url = new URL("https://api.football-data.org/v4/matches/" + match.getApiId());
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("X-Auth-Token", TOKEN);
        con.setDoOutput(true);
        con.connect();
        InputStream inputStream = con.getInputStream();
        JSONParser jsonParser = new JSONParser();
        JSONObject matchJson = (JSONObject) jsonParser.parse(new InputStreamReader(inputStream, "UTF-8"));

        JSONObject score = (JSONObject) matchJson.get("score");
        JSONObject fullTime = (JSONObject) score.get("fullTime");
        JSONObject halfTime = (JSONObject) score.get("halfTime");

        String matchDetails = "Match " + match.getId() + " -> " + match.getHome().getShortName() + "x" + match.getAway().getShortName();

        try {
            if (fullTime.get("home") != null) {
                match.setScoreHome((Integer) fullTime.get("home"));
                match.setScoreAway((Integer) fullTime.get("away"));
                log.info(matchDetails + " is in 2nd half: " + match.getScoreHome() + ":" + match.getScoreAway());
            } else if (fullTime.get("home") != null) {
                match.setScoreHome((Integer) halfTime.get("home"));
                match.setScoreAway((Integer) halfTime.get("away"));
                log.info(matchDetails + " is in 1st half: " + match.getScoreHome() + ":" + match.getScoreAway());
            } else {
                log.info(matchDetails + " has not started.");
                match.setScoreHome(2);
                match.setScoreAway(0);
            }
        } catch (Exception e) {
            match.setScoreHome(-1);
            match.setScoreAway(-1);
        }

        try {
            match.persistAndFlush();
        } catch (Exception e) {
            log.severe("Error during automatic update: " + e.getMessage());
        }
    }
}
