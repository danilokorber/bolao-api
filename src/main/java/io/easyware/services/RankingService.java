package io.easyware.services;

import io.easyware.entities.Bet;
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
    TournamentEditionService tournamentEditionService;

    private Map<Long, List<Ranking>> ranking = new HashMap<Long, List<Ranking>>();
    ;

    public List<Ranking> rankingOf(TournamentEdition tournamentEdition) {
        List<Ranking> r = ranking.get(tournamentEdition.getId());
        r.sort(Comparator.comparingInt(Ranking::getPoints).reversed());
        return r;
    }

    public Map<String, Object> userRank(KeycloakUser user, TournamentEdition tournamentEdition) throws IOException {
        Map<String, Object> res = new HashMap<>();
        List<Ranking> ranking = prepareFor(tournamentEdition);
        ranking.forEach(r -> {
            if (r.getUser().getEmail().equals(user.getEmail())) {
                res.put("position", ranking.indexOf(r) + 1);
                res.put("ranking", r);
            }
        });
        return res;
    }

    //@Scheduled(every = "20s")
    public void prepareRanking() {
        log.info("Updating ranking");
        List<TournamentEdition> all = tournamentEditionService.findAll();
        log.info("Updating ranking for " + all.size() + " tournaments.");
        ranking.clear();
        log.info("Ranking has " + ranking.size() + " entries.");
        all.forEach(t -> {
            log.info("  Updating " + t.getUrl());
            try {
                ranking.put(t.getId(), prepareFor(t));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            log.fine("Ranking for " + t.getName() + " updated on " + new Date() + ".");
        });
    }

    public List<Ranking> prepareFor(TournamentEdition tournamentEdition) throws IOException {
        List<Ranking> ranking = new ArrayList<Ranking>();

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
                List<Bet> bets = betService.getAllBetsFromUserForTournamentEdition(user.getId(), tournamentEdition);
                newRanking.setBets(bets);
                int points = 0;
                if (bets.size() > 0) {
                    points = bets.stream()
                            .map(Bet::getPoints)
                            .reduce(0, Integer::sum);
                }
                newRanking.setPoints(points);

//                Date now = new Date();
//                Date startThirdRound = new Date(2022, 11, 28, 15,0,0);
//                if (now.before(startThirdRound)) newRanking.getUser().setAttributes(null);

                ranking.add(newRanking);
            }
        });

        ranking.sort(Comparator.comparingInt(Ranking::getPoints).reversed());
        log.info("ranking has size " + ranking.size());
        return ranking;
    }



}
