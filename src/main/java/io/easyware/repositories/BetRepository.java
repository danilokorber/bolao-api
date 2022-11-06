package io.easyware.repositories;

import io.easyware.entities.Bet;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class BetRepository  implements PanacheRepository<Bet> {
}
