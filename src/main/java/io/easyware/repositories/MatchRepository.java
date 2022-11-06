package io.easyware.repositories;

import io.easyware.entities.Match;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class MatchRepository implements PanacheRepository<Match> {
}
