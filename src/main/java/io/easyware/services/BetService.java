package io.easyware.services;

import io.easyware.entities.Bet;
import io.easyware.entities.Match;
import io.easyware.entities.TournamentEdition;
import io.easyware.repositories.BetRepository;
import io.easyware.shared.keycloak.KeycloakService;
import io.easyware.shared.keycloak.KeycloakUser;
import lombok.extern.java.Log;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
@Log
public class BetService {

    private final BetRepository betRepository;
    @Inject
    MatchService matchService;

    @Inject
    KeycloakService keycloakService;

    public BetService(BetRepository betRepository) {
        this.betRepository = betRepository;
    }

    public List<Bet> findAll() {
        return betRepository.findAll().stream().collect(Collectors.toList());
    }

    @Transactional
    public Bet save(Bet bet) {
        bet.persistAndFlush();
        return bet;
    }

    @Transactional
    public void disableBetsFromUser(String userId, long matchId) {
        betRepository.update("active = false where userId = ?1 AND matchId = ?2", userId, matchId);
    }

    @Transactional
    public List<Bet> getAllBetsFromUserForMatch(String userId, long matchId) {
        return betRepository.find("matchId = ?1 AND userId = ?2", matchId, userId).list();
    }

    @Transactional
    public List<Bet> findValidBetsOfMatch(Match match) {
        return findValidBetsOfMatch(match, null);
    }

    @Transactional
    public List<Bet> findValidBetsOfMatch(Match match, String userId) {
        if (match.getKickoff().before(new Date())) {
            List<Bet> bets = betRepository.find("active = ?1 AND matchId = ?2", true, match.getId()).list();
            bets.forEach(bet -> bet.setPoints(PointsService.calculatePoints(match, bet)));
            return bets;
        } else {
            if (userId != null) {
                List<Bet> bets = betRepository.find("active = ?1 AND matchId = ?2 AND userId = ?3", true, match.getId(), userId).list();
                bets.forEach(bet -> bet.setPoints(PointsService.calculatePoints(match, bet)));
                return bets;
            } else {
                return null;
            }
        }
    }

    public void updateBetsForTournamentEdition(TournamentEdition tournamentEdition, String userId) {
        tournamentEdition.getMatches().forEach(match -> {
            match.setBets(findValidBetsOfMatch(match, userId));
        });
    }

    public List<Bet> getAllBetsFromUserForTournamentEdition(String userId, TournamentEdition tournamentEdition) {
        List<Bet> allBets = betRepository.find("active = ?1 AND userId = ?2", true, userId).list();
         List<Bet> bets = allBets.stream().filter(b -> {
            Match match = matchService.find(b.getMatchId());
            return match.getKickoff().before(new Date());
        }).collect(Collectors.toList());
        bets.forEach(bet -> bet.setPoints(PointsService.calculatePoints(matchService.find(bet.getMatchId()), bet)));
        return bets.stream().filter(bet -> matchService.matchIsFromTournamentEditions(bet.getMatchId(), tournamentEdition.getId())).collect(Collectors.toList());
    }

}
