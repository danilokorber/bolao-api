package io.easyware.services;

import io.easyware.entities.Match;
import io.easyware.repositories.MatchRepository;
import lombok.extern.java.Log;
import org.jose4j.json.internal.json_simple.JSONObject;
import org.jose4j.json.internal.json_simple.parser.JSONParser;
import org.jose4j.json.internal.json_simple.parser.ParseException;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
@Log
public class MatchService {
    private final String TOKEN = "1a8ebe538b1d4c71a2ea2e30e518236e";

    private final MatchRepository matchRepository;

    public MatchService(MatchRepository matchRepository) {
        this.matchRepository = matchRepository;
    }

    public List<Match> findAll() {
        return matchRepository.findAll().list();
    }

    public Match find(long matchId) {
        return matchRepository.findById(matchId);
    }

    public boolean matchIsFromTournamentEditions(long matchId, long tournamentEditionId) {
        Match match = find(matchId);
        return match.getTournamentEditionId() == tournamentEditionId;
    }

    public List<Match> liveMatches() {
        return matchRepository.listAll().stream().filter(m -> {
            boolean started = m.getKickoff().before(new Date());
            if (!started) return false;

            final int MILLI_TO_HOUR = 1000 * 60 * 60;
            Date now = new Date();
            float dif = (now.getTime() - m.getKickoff().getTime()) / MILLI_TO_HOUR;
            boolean finished = dif >= 3;
            return !finished;
        }).collect(Collectors.toList());
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
            match.persist();
        } catch (Exception e) {
            log.severe(e.getMessage());
        }
    }
}
