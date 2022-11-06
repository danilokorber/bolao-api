package io.easyware.boundary;

import io.easyware.shared.keycloak.KeycloakService;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

@Consumes(APPLICATION_JSON)
@Produces(APPLICATION_JSON)
public class UserApi {

    @Inject
    KeycloakService keycloakService;

    @Context
    SecurityContext sec;
}
