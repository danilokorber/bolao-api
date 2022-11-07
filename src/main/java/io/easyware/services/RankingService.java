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
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
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

    private Map<Long, List<Ranking>> ranking  = new HashMap<Long, List<Ranking>>();;

    public List<Ranking> rankingOf(TournamentEdition tournamentEdition) {
        List<Ranking> r = ranking.get(tournamentEdition.getId());
        r.sort(Comparator.comparingInt(Ranking::getPoints).reversed());
        return r;
    }

    public Map<String, Object> userRank(KeycloakUser user, TournamentEdition tournamentEdition) {
        Map<String, Object> res = new HashMap<>();
        List<Ranking> ranking = rankingOf(tournamentEdition);
        ranking.forEach(r -> {
            if (r.getUser().getEmail().equals(user.getEmail())) {
                res.put("position", ranking.indexOf(r) + 1);
                res.put("ranking", r);
            }
        });
        return res;
    }

    @Scheduled(every = "10s")
    public void prepareRanking() {
        List<TournamentEdition> all = tournamentEditionService.findAll();
        all.forEach(t -> {
            ranking.put(t.getId(), prepareFor(t));
            log.fine("Ranking for " + t.getName() + " updated on " + new Date() + ".");
        });
    }

    private List<Ranking> prepareFor(TournamentEdition tournamentEdition) {
        List<Ranking> ranking = new ArrayList<Ranking>();

        List<KeycloakUser> users = keycloakService.findAll();
        users.forEach(user -> {

            if (user.getAttributes() != null && user.getAttributes().getPayment_on() != null && user.getAttributes().getPayment_on().size() > 0) {

Date paymentDate = new Date(String.valueOf(user.getAttributes().getPayment_on().get(0)));
            Ranking newRanking = new Ranking();
            newRanking.setUser(user);
            List<Bet> bets = betService.getAllBetsFromUserForTournamentEdition(user.getId(), tournamentEdition); //.stream().filter(b -> b.getBetPlacedOn().after(paymentDate)).collect(Collectors.toList());
            newRanking.setBets(bets);
            int points = 0;
            if (bets.size() > 0) {
                points = bets.stream()
                        .map(Bet::getPoints)
                        .reduce(0, Integer::sum);
            }
            newRanking.setPoints(points);

            log.info(newRanking.toString());
            ranking.add(newRanking);
            }
        });

        ranking.sort(Comparator.comparingInt(Ranking::getPoints).reversed());

        return ranking; //.stream().filter(r -> r.getUser().getAttributes().getPayment_on() != null).collect(Collectors.toList());
    }

}
