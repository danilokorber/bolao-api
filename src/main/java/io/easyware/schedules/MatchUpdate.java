package io.easyware.schedules;

import io.easyware.entities.Bet;
import io.easyware.services.BetService;
import io.easyware.services.MatchService;
import io.quarkus.scheduler.Scheduled;
import lombok.extern.java.Log;
import org.jose4j.json.internal.json_simple.JSONObject;
import org.jose4j.json.internal.json_simple.parser.JSONParser;
import org.jose4j.json.internal.json_simple.parser.ParseException;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
@Log
public class MatchUpdate {

    @Inject
    BetService betService;

    @Inject
    MatchService matchService;

    // https://quarkus.io/guides/scheduler
    // https://docs.oracle.com/cd/E12058_01/doc/doc.1014/e12030/cron_expressions.htm

    private boolean isActive = true;
    private final String TOKEN = "1a8ebe538b1d4c71a2ea2e30e518236e";


    @Scheduled(every = "15s")
    @Transactional
    void increment() {
        if (isActive) {
            log.info("Updating matches");
            matchService.liveMatches().forEach(match -> {
                try {
                    String matchDetails = "Updating match " + match.getId() + " -> " + match.getHome().getShortName() + " x " + match.getAway().getShortName();
                    log.info(matchDetails);

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

                    try {
                        match.setStatus(matchJson.get("status").toString());
                        log.info(matchDetails + " status: " + match.getStatus());

                        if (fullTime.get("home") != null) {
                            log.info("     fullTime: " + fullTime);
                            match.setScoreHome(Integer.parseInt(fullTime.get("home").toString()));
                            match.setScoreAway(Integer.parseInt(fullTime.get("away").toString()));
                            log.info(matchDetails + " is in 2nd half: " + match.getScoreHome() + ":" + match.getScoreAway());
                        } else if (halfTime.get("home") != null) {
                            match.setScoreHome(Integer.parseInt(halfTime.get("home").toString()));
                            match.setScoreAway(Integer.parseInt(halfTime.get("home").toString()));
                            log.info(matchDetails + " is in 1st half: " + match.getScoreHome() + ":" + match.getScoreAway());
                        } else {
                            log.info(matchDetails + " has not started.");
                            match.setScoreHome(2);
                            match.setScoreAway(0);
                        }
                    } catch (Exception e) {
                        log.severe("Error during reading: " + e.getMessage());
                        match.setScoreHome(-1);
                        match.setScoreAway(-1);
                    }


                    try {
                        match.persistAndFlush();
                    } catch (Exception e) {
                        log.severe("Error during persistence: " + e.getMessage());
                    }


                } catch (IOException | ParseException e) {
                    throw new RuntimeException(e);
                }
            });
        }
    }

//    @Scheduled(cron = "0 20 15 * * ?")
//    void activate() {
//        isActive = true;
//        log.info("Job started");
//        increment();
//    }
//
//    @Scheduled(cron = "0 22 15 * * ?")
//    void deactivate() {
//        isActive = false;
//        log.info("Job stopped");
//    }
}
