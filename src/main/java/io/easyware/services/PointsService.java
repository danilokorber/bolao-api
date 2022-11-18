package io.easyware.services;

import io.easyware.entities.Bet;
import io.easyware.entities.Match;
import lombok.extern.java.Log;

import java.util.Date;
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
                    if (bet.getScoreHome() - bet.getScoreAway() == match.getScoreHome() - match.getScoreAway()) return 10;
                    //log.info("Goal difference is correct: FAILED");

                    // One of the scores is correct
                    if (bet.getScoreHome() == match.getScoreHome() || bet.getScoreAway() == match.getScoreAway()) return 7;
                    //log.info("One of the scores is correct: FAILED");

                    // Result correct, either goal difference nor one of the scores is correct
                    //log.info("Result correct, either goal difference nor one of the scores is correct: SUCCESS");
                    return 4;
                };
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
}
