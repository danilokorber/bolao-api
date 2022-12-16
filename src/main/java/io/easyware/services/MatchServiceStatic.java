package io.easyware.services;

import io.easyware.entities.Match;
import io.easyware.entities.Team;
import io.easyware.repositories.MatchRepository;
import io.easyware.repositories.TeamsRepository;
import lombok.extern.java.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Log
public class MatchServiceStatic {

    private static MatchRepository matchRepository = null;

    public MatchServiceStatic(MatchRepository matchRepository) {
        MatchServiceStatic.matchRepository = matchRepository;
    }

    public static Optional<Match> find(long id) {
        log.info("Looking for match " + id + ".");
        return Optional.ofNullable(matchRepository.findById(id));
    }

    public static List<Match> findAll() {
        List<Match> matches = matchRepository.findAll().list();
        log.info("Found " + matches.size() + " matches.");
        return matchRepository.findAll().list();
    }

    public static Optional<Match> getSingle(long id) {
        return findAll().stream().filter(m -> m.getId() == id).findFirst();
    }
}
