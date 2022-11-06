package io.easyware.boundary;

import io.easyware.services.BetService;
import io.easyware.services.MatchService;
import io.easyware.services.RankingService;
import io.easyware.services.TournamentEditionService;
import io.easyware.services.TournamentService;
import io.easyware.shared.keycloak.KeycloakService;
import io.quarkus.oidc.IdToken;
import org.eclipse.microprofile.jwt.JsonWebToken;

import javax.inject.Inject;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;

public class TournamentsApi {

    @Inject
    @IdToken
    JsonWebToken idToken;
    @Inject
    TournamentService tournamentService;

    @Inject
    TournamentEditionService tournamentEditionService;

    @Inject
    BetService betService;

    @Inject
    KeycloakService keycloakService;

    @Inject
    MatchService matchService;

    @Inject
    RankingService rankingService;

    @Context
    SecurityContext sec;

    final String REGEX_SLUG = "^([a-zA-Z0-9-])+$";
}
