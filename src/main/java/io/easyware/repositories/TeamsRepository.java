package io.easyware.repositories;

import io.easyware.entities.Team;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class TeamsRepository implements PanacheRepository<Team> {
}
