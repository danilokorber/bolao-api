package io.easyware.boundary;

import io.easyware.entities.Match;
import io.easyware.entities.Team;
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

@Path("v1/matches")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class MatchApiV1 extends MatchApi {

    @GET
    @Path("{matchId}")
    @Operation(summary = "Get details of a given match based on its id.")
    @APIResponse(
            responseCode = "200",
            description = "OK",
            content = @Content(mediaType = "application/json"))
    @APIResponse(
            responseCode = "400",
            description = "Bad request",
            content = @Content(mediaType = "application/json"))
    @APIResponse(
            responseCode = "404",
            description = "Not found")
    public Response findByTeamId(
            @Parameter(description = "Match ID", required = true, example = "12", schema = @Schema(type = SchemaType.NUMBER))
            @PathParam("matchId")
            int matchId
    ) {
        Match match = this.matchService.find(matchId);
        if (match != null) return Response.ok().entity(match).build();
        return Response.noContent().build();
    }
}
