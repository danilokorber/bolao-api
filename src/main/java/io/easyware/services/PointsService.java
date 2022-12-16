package io.easyware.services;

import io.easyware.entities.Bet;
import io.easyware.entities.Match;
import io.easyware.entities.Team;
import io.easyware.shared.keycloak.KeycloakUser;
import lombok.extern.java.Log;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Log
public class PointsService {

    public static Integer calculatePoints(Bet bet) {
        Optional<Match> match = MatchServiceStatic.find(bet.getMatchId());
        if (match.isEmpty()) return 0;
        return calculatePoints(match.get(), bet);
    }

    public static Integer calculatePoints(Match match, Bet bet) {
        Date now = new Date();

        try {
            if (match != null &&
                    match.getKickoff().before(now) &&
                    match.getScoreHome() != null &&
                    match.getScoreAway() != null) {

                //log.info("Official Score: " + match.getScoreHome() + ":" + match.getScoreAway() + " (match " + match.getId() + ")");
                //log.info("Bet Score: " + bet.getScoreHome() + ":" + bet.getScoreAway() + " (bet " + bet.getId() + ")");

                // Result and scores are correct
                if (bet.getScoreHome() == match.getScoreHome() && bet.getScoreAway() == match.getScoreAway()) return 20;
                //log.info("Result and scores are correct: FAILED");

                // Result is correct (no draw)
                if ((bet.getScoreHome() - bet.getScoreAway() > 0 && match.getScoreHome() - match.getScoreAway() > 0) ||
                        (bet.getScoreHome() - bet.getScoreAway() < 0 && match.getScoreHome() - match.getScoreAway() < 0)) {
                    //log.info("Result is correct (no draw): SUCCESS");

                    // Goal difference is correct
                    if (bet.getScoreHome() - bet.getScoreAway() == match.getScoreHome() - match.getScoreAway())
                        return 10;
                    //log.info("Goal difference is correct: FAILED");

                    // One of the scores is correct
                    if (bet.getScoreHome() == match.getScoreHome() || bet.getScoreAway() == match.getScoreAway())
                        return 7;
                    //log.info("One of the scores is correct: FAILED");

                    // Result correct, either goal difference nor one of the scores is correct
                    //log.info("Result correct, either goal difference nor one of the scores is correct: SUCCESS");
                    return 4;
                }
                ;
                //log.info("Result is correct (no draw): FAILED");

                // Result is correct (draw)
                if (bet.getScoreHome() == bet.getScoreAway() && match.getScoreHome() == match.getScoreAway()) return 7;
                //log.info("Result is correct (draw): FAILED");

                // Inverted result
                if (bet.getScoreHome() == match.getScoreAway() && bet.getScoreAway() == match.getScoreHome()) return 2;
                //log.info("Inverted result: FAILED");

                // One of the scores is correct
                if (bet.getScoreHome() == match.getScoreHome() || bet.getScoreAway() == match.getScoreAway()) return 1;
                //log.info("One of the scores is correct: FAILED");

                // Completely wrong
                return 0;
            }
        } catch (Exception e) {
            log.severe(e.getMessage());
            return 0;
        }

        //log.warning("Match not started or without result information.");
        return 0;
    }

    public static Integer calculateBonusPoints(KeycloakUser user, List<Match> matches) {
        String bet1 = user.getAttributes().getFirst().get(0);
        String bet2 = user.getAttributes().getSecond().get(0);
        String bet3 = user.getAttributes().getThird().get(0);
        String bet4 = user.getAttributes().getFourth().get(0);

        long id3 = 63;
        long id1 = 64;

        Optional<Match> match3 = matches.stream().filter(m -> m.getId() == id3).findFirst();
        Optional<Match> match1 = matches.stream().filter(m -> m.getId() == id1).findFirst();
        Team t1 = null;
        Team t2 = null;
        Team t3 = null;
        Team t4 = null;

        Integer bonusPoints = 0;

        if (match3.isPresent() && match3.get().getStatus() != null && match3.get().getStatus().equals("FINISHED")) {
            Match m = match3.get();
            if (m.getScoreHome() == m.getScoreAway()) {
                if (m.getPsoHome() > m.getPsoAway()) {
                    t3 = m.getHome();
                    t4 = m.getAway();
                } else {
                    t3 = m.getAway();
                    t4 = m.getHome();
                }
            } else {
                if (m.getScoreHome() > m.getScoreAway()) {
                    t3 = m.getHome();
                    t4 = m.getAway();
                } else {
                    t3 = m.getAway();
                    t4 = m.getHome();
                }
            }
        } else {
            if (bet4.equals("ARG") ||
                    bet4.equals("FRA") ||
                    bet4.equals("CRO") ||
                    bet4.equals("MAR"))
                bonusPoints += 5;
            if (bet3.equals("ARG") ||
                    bet3.equals("FRA") ||
                    bet3.equals("CRO") ||
                    bet3.equals("MAR"))
                bonusPoints += 5;
        }

        if (match1.isPresent() && match1.get().getStatus() != null && match1.get().getStatus().equals("FINISHED")) {
            Match m = match1.get();
            if (m.getScoreHome() == m.getScoreAway()) {
                if (m.getPsoHome() > m.getPsoAway()) {
                    t1 = m.getHome();
                    t2 = m.getAway();
                } else {
                    t1 = m.getAway();
                    t2 = m.getHome();
                }
            } else {
                if (m.getScoreHome() > m.getScoreAway()) {
                    t1 = m.getHome();
                    t2 = m.getAway();
                } else {
                    t1 = m.getAway();
                    t2 = m.getHome();
                }
            }
        }else {
            if (bet2.equals("ARG") ||
                    bet2.equals("FRA") ||
                    bet2.equals("CRO") ||
                    bet2.equals("MAR"))
                bonusPoints += 5;
            if (bet1.equals("ARG") ||
                    bet1.equals("FRA") ||
                    bet1.equals("CRO") ||
                    bet1.equals("MAR"))
                bonusPoints += 5;
        }

        if (t3 != null && t4 != null) {
            if (t4.getShortName().equals(bet4)) {
                bonusPoints += 10;
            } else if (t4.getShortName().equals(bet3) || t4.getShortName().equals(bet2) || t4.getShortName().equals(bet1)) {
                bonusPoints += 5;
            }
            if (t3.getShortName().equals(bet3)) {
                bonusPoints += 10;
            } else if (t3.getShortName().equals(bet4) || t3.getShortName().equals(bet2) || t3.getShortName().equals(bet1)) {
                bonusPoints += 5;
            }
        }

        if (t1!= null && t2!= null) {
            if (t2.getShortName().equals(bet2)) {
                bonusPoints += 10;
            } else if (t2.getShortName().equals(bet4) || t2.getShortName().equals(bet3) || t2.getShortName().equals(bet1)) {
                bonusPoints += 5;
            }
            if (t1.getShortName().equals(bet1)) {
                bonusPoints += 20;
            } else if (t1.getShortName().equals(bet4) || t1.getShortName().equals(bet3) || t1.getShortName().equals(bet2)) {
                bonusPoints += 5;
            }
        }

        return bonusPoints;
    }
}
