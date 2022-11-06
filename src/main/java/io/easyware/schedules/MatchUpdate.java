package io.easyware.schedules;

import io.easyware.entities.Bet;
import io.easyware.services.BetService;
import io.quarkus.scheduler.Scheduled;
import lombok.extern.java.Log;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
@Log
public class MatchUpdate {

    @Inject
    BetService betService;

    // https://quarkus.io/guides/scheduler
    // https://docs.oracle.com/cd/E12058_01/doc/doc.1014/e12030/cron_expressions.htm

    private boolean isActive = false;

    @Scheduled(every = "10s")
    void increment() {
        if (isActive) {
            log.info("Updating match results");
        }
    }

    @Scheduled(cron = "0 20 15 * * ?")
    void activate() {
        isActive = true;
        log.info("Job started");
        increment();
    }

    @Scheduled(cron = "0 22 15 * * ?")
    void deactivate() {
        isActive = false;
        log.info("Job stopped");
    }
}
