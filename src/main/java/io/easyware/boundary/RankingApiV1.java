package io.easyware.boundary;

import io.easyware.entities.Ranking;
import io.easyware.entities.TournamentEdition;
import io.easyware.shared.keycloak.KeycloakUser;
import io.quarkus.security.Authenticated;
import lombok.extern.java.Log;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;

import javax.validation.constraints.Pattern;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.List;

@Path("v1/ranking")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Log
public class RankingApiV1 extends RankingApi {

    @GET
    @Operation(summary = "Gets ranking of the bets for a given tournament edition.")
    @APIResponse(
            responseCode = "200",
            description = "OK",
            content = @Content(mediaType = "application/json"))
    @APIResponse(
            responseCode = "204",
            description = "No content")
    @APIResponse(
            responseCode = "400",
            description = "Bad request",
            content = @Content(mediaType = "application/json"))
    public Response ranking() throws IOException {
        TournamentEdition tournamentEdition = this.tournamentEditionService.getTournamentById(1);
        List<Ranking> ranking = rankingService.prepareFor(tournamentEdition);
        return ranking.isEmpty() ? Response.noContent().build() : Response.ok().entity(ranking).build();
    }

    @GET
    @Path("position")
    @Authenticated
    @Operation(summary = "Get position for user in ranking")
    @APIResponse(
            responseCode = "200",
            description = "OK",
            content = @Content(mediaType = "application/json"))
    public Response getRanking() throws IOException {
        TournamentEdition tournamentEdition = this.tournamentEditionService.getTournamentById(1);
        KeycloakUser user = keycloakService.findByMail(sec.getUserPrincipal().getName());

        return Response.ok(rankingService.userRank(user, tournamentEdition)).build();
    }
}
