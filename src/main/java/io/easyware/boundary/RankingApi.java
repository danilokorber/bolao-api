package io.easyware.boundary;

import io.easyware.services.BetService;
import io.easyware.services.RankingService;
import io.easyware.services.TournamentEditionService;
import io.easyware.shared.keycloak.KeycloakService;

import javax.inject.Inject;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;

public class RankingApi {

    @Context
    SecurityContext sec;

    @Inject
    KeycloakService keycloakService;

    @Inject
    RankingService rankingService;

    @Inject
    TournamentEditionService tournamentEditionService;
}
