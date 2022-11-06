package io.easyware.services;

import com.sun.xml.bind.v2.model.core.ID;
import io.easyware.entities.Match;
import io.easyware.repositories.MatchRepository;
import lombok.extern.java.Log;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
@Log
public class MatchService {

    private final MatchRepository matchRepository;

    public MatchService(MatchRepository matchRepository) {
        this.matchRepository = matchRepository;
    }

    public Match find(long matchId) {
        return matchRepository.findById(matchId);
    }

    public boolean matchIsFromTournamentEditions(long matchId, long tournamentEditionId) {
        Match match = find(matchId);
        return match.getTournamentEditionId() == tournamentEditionId;
    }
}
