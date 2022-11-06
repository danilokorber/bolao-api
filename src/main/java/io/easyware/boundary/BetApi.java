package io.easyware.boundary;

import io.easyware.services.BetService;
import io.easyware.services.MatchService;
import io.easyware.shared.keycloak.KeycloakService;
import io.easyware.shared.log.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;

@ApplicationScoped
public class BetApi {

    @Inject
    BetService betService;

    @Inject
    MatchService matchService;

    @Inject
    KeycloakService keycloakService;

    @Context
    SecurityContext sec;

    @Inject
    Logger log;
}
