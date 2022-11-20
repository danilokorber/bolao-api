package io.easyware.boundary;

import io.easyware.services.MatchService;
import io.easyware.shared.keycloak.KeycloakService;

import javax.inject.Inject;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;

public class MatchApi {

    @Context
    SecurityContext sec;
    @Inject
    MatchService matchService;

    @Inject
    KeycloakService keycloakService;
}
