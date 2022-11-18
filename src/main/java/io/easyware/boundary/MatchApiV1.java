package io.easyware.boundary;

import io.easyware.entities.Match;
import lombok.extern.java.Log;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Log
@Path("v1/matches")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class MatchApiV1 extends MatchApi {

    @GET
    @Operation(summary = "Get all matches.")
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
    public Response findAll(@Parameter(description = "Next x matches in this tournamentEdition.", example = "4", schema = @Schema(type = SchemaType.INTEGER))
                            @QueryParam("next")
                            int next) {
        List<Match> matches = matchService.findAll();
        if (matches.isEmpty()) return Response.noContent().build();

        if (next == 0) {
            return Response.ok().entity(matches).build();
        } else {
            List<Match> nextMatches = matches.stream().filter(m -> m.getKickoff().after(new Date())).collect(Collectors.toList());
            int lastIndex = next > nextMatches.size() ? nextMatches.size() : next;
            return Response.ok().entity(nextMatches.subList(0, lastIndex)).build();
        }
    }

    @GET
    @Path("live")
    @Operation(summary = "Get all live matches.")
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
    public Response findLive() {
        List<Match> matches = matchService.findAll().stream().filter(m -> m.isLive()).collect(Collectors.toList());
        if (matches.isEmpty()) return Response.ok().entity(new ArrayList<>()).build();
        return Response.ok().entity(matches).build();
    }

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