package io.easyware.boundary;

import io.easyware.services.BetService;
import io.easyware.services.RankingService;
import io.easyware.services.TournamentEditionService;
import io.easyware.shared.keycloak.KeycloakService;

import javax.inject.Inject;

public class RankingApi {

    @Inject
    KeycloakService keycloakService;

    @Inject
    RankingService rankingService;

    @Inject
    TournamentEditionService tournamentEditionService;
}
