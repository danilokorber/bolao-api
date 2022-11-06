package io.easyware.services;

import io.easyware.entities.Tournament;
import io.easyware.entities.TournamentEdition;
import io.easyware.repositories.TournamentEditionRepository;
import io.easyware.repositories.TournamentRepository;
import io.easyware.shared.Slug;
import io.easyware.shared.keycloak.KeycloakService;
import lombok.extern.java.Log;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@ApplicationScoped
@Log
public class TournamentEditionService implements Slug {

    @Context
    SecurityContext sec;

    @Inject
    KeycloakService keycloakService;

    private final TournamentEditionRepository tournamentEditionRepository;
    private final TournamentRepository tournamentRepository;

    public TournamentEditionService(TournamentEditionRepository tournamentEditionRepository, TournamentRepository tournamentRepository) {
        this.tournamentEditionRepository = tournamentEditionRepository;
        this.tournamentRepository = tournamentRepository;
    }

    public List<TournamentEdition> findAll() {
        return tournamentEditionRepository.findAll().list();
    }

    public TournamentEdition getTournamentById(long id) {
        return tournamentEditionRepository.findById(id);
    }

    public TournamentEdition getTournamentBySlug(String tournamentSlug, String editionSlug) {
        TournamentEdition tournamentEdition = null;

        List<Tournament> tournaments = tournamentRepository.listAll()
                .stream()
                .filter(t -> slugify(t.getName()).equalsIgnoreCase(tournamentSlug))
                .collect(Collectors.toList());

        if (tournaments.size() == 1) {
            tournamentEdition = tournaments.get(0).getEditions().stream()
                    .filter(e -> slugify(e.getName()).equalsIgnoreCase(editionSlug))
                    .findFirst().get();
        }

        return tournamentEdition;
    }
}
