package io.easyware.services;

import io.easyware.entities.Match;
import io.easyware.repositories.MatchRepository;
import lombok.extern.java.Log;

import java.util.Optional;

@Log
public class MatchServiceStatic {

    private static MatchRepository matchRepository = null;

    public MatchServiceStatic(MatchRepository matchRepository) {
        MatchServiceStatic.matchRepository = matchRepository;
    }

    public static Optional<Match> find(long id) {
        return Optional.ofNullable(matchRepository.findById(id));
    }
}
