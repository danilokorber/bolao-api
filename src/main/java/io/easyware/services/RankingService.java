package io.easyware.services;

import io.easyware.entities.Bet;
import io.easyware.entities.Match;
import io.easyware.entities.Ranking;
import io.easyware.entities.TournamentEdition;
import io.easyware.shared.keycloak.KeycloakService;
import io.easyware.shared.keycloak.KeycloakUser;
import io.quarkus.scheduler.Scheduled;
import lombok.extern.java.Log;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@ApplicationScoped
@Log
public class RankingService {

    @Inject
    KeycloakService keycloakService;

    @Inject
    BetService betService;

    @Inject
    MatchService    matchService;

    @Inject
    TournamentEditionService tournamentEditionService;

    private Map<Long, List<Ranking>> ranking = new HashMap<Long, List<Ranking>>();
    ;

    public List<Ranking> rankingOf(TournamentEdition tournamentEdition) {
        List<Ranking> r = ranking.get(tournamentEdition.getId());
        r.sort(Comparator.comparingInt(Ranking::getPoints).reversed());
        return r;
    }

    public Map<String, Object> userRank(KeycloakUser user, TournamentEdition tournamentEdition) throws IOException {
        return userRank(user, tournamentEdition, new Date());
    }

    public Map<String, Object> userRank(KeycloakUser user, TournamentEdition tournamentEdition, Date date) throws IOException {
        Map<String, Object> res = new HashMap<>();
        List<Ranking> ranking = prepareFor(tournamentEdition, date);
        ranking.sort(Comparator.comparingInt(Ranking::getPoints).reversed());
        ranking.forEach(r -> {
            if (r.getUser().getEmail().equals(user.getEmail())) {
                res.put("position", ranking.indexOf(r) + 1);
                res.put("ranking", r);
            }
        });
        return res;
    }

    public List<Ranking> prepareFor(TournamentEdition tournamentEdition) throws IOException {
        return prepareFor(tournamentEdition, new Date());
    }

    public List<Ranking> prepareFor(TournamentEdition tournamentEdition, Date date) throws IOException {
        List<Ranking> ranking = new ArrayList<Ranking>();
        List<Match> matches = matchService.findAll();

        List<KeycloakUser> users = keycloakService.findAll();
        List<KeycloakUser> payees = users.stream()
                .filter(user ->
                        user.getAttributes() != null &&
                        user.getAttributes().getPayment_on() != null &&
                        user.getAttributes().getPayment_on().size() > 0)
                .collect(Collectors.toList());
        log.info("user list has size " + users.size() + " and payees list has size " + payees.size());
        if (payees.isEmpty()) {
            log.info(users.stream().filter(u -> u.getEmail().equals("danilo@korber.com.br")).findFirst().toString());
        }
        users.forEach(user -> {

            if (user.getAttributes() != null && user.getAttributes().getPayment_on() != null && user.getAttributes().getPayment_on().size() > 0) {

                Ranking newRanking = new Ranking();
                newRanking.setUser(user);
                List<Bet> bets = betService.getAllBetsFromUserForTournamentEdition(user.getId(), tournamentEdition, date);
                newRanking.setBets(bets);
                int points = 0;
                if (bets.size() > 0) {
                    points = bets.stream()
                            .map(Bet::getPoints)
                            .reduce(0, Integer::sum);
                }
                newRanking.setBonusPoints(PointsService.calculateBonusPoints(user, matches));
                newRanking.setPoints(points + newRanking.getBonusPoints());

                ranking.add(newRanking);
            }
        });

        ranking.sort(Comparator.comparingInt(Ranking::getPoints).reversed());
        log.info("ranking has size " + ranking.size());
        return ranking;
    }



}
