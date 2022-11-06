package io.easyware.repositories;

import io.easyware.entities.Tournament;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class TournamentRepository implements PanacheRepository<Tournament> {
}
