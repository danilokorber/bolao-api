package io.easyware.repositories;

import io.easyware.entities.TournamentEdition;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class TournamentEditionRepository implements PanacheRepository<TournamentEdition> {
}
