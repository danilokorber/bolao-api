package io.easyware.services;

import io.easyware.entities.Tournament;
import io.easyware.repositories.TournamentRepository;
import io.easyware.shared.Slug;

import javax.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
public class TournamentService implements Slug {

    private final TournamentRepository tournamentRepository;

    TournamentService(TournamentRepository tournamentRepository) {
        this.tournamentRepository = tournamentRepository;
    }

    public List<Tournament> getAllTournaments() { return tournamentRepository.listAll(); }

    public String idToSlug(long tournamentId) {
        return slugify(tournamentRepository.findById(tournamentId).getName());
    }
}
