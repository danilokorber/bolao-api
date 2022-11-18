package io.easyware.boundary;

import io.easyware.entities.Bet;
import io.easyware.entities.Match;
import io.easyware.entities.Ranking;
import io.easyware.entities.Tournament;
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
import org.jose4j.json.internal.json_simple.parser.ParseException;

import javax.validation.constraints.Pattern;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Path("v1/tournaments")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Log
//@Authenticated
public class TournamentsApiV1 extends TournamentsApi {


    private final String DESC_ALL = "Gets all tournaments from the database.";

    private final String DESCRIPTION_ID = "ID of tournament edition.";


    private final String INVALID_SLUG = "Invalid slug. Must be a string with letters form a-z, 0-9 or -.";

    private final String DESC_EDITION_SINGLE_ID = "Gets tournament edition based by id.";
    private final String PATH_EDITION_SINGLE_ID = "{tournamentSlug}/editions/id/{id}";

    private final String DESC_EDITION_MATCHES = "Gets matches of tournament edition.";
    private final String PATH_EDITION_MATCHES_ID = "{tournamentSlug}/editions/id/{id}/matches";

    private final String DESC_EDITION_GROUPS = "Gets groups of tournament edition.";
    private final String PATH_EDITION_GROUPS_SLUG = "{tournamentSlug}/editions/{editionSlug}/groups";
    private final String PATH_EDITION_GROUPS_ID = "{tournamentSlug}/editions/id/{id}/groups";

    @GET
    @Path("getid")
    public Response getId() throws IOException, ParseException {
        tournamentService.getId();
        return Response.ok().build();
    }

    @GET
    @Operation(summary = DESC_ALL)
    @APIResponse(
            responseCode = "200",
            description = "OK",
            content = @Content(mediaType = "application/json"))
    public Response all() {
        List<Tournament> teams = this.tournamentService.getAllTournaments();
        return Response.ok().entity(teams).build();
    }

    @GET
    @Path("{tournamentSlug}/editions/{editionSlug}")
    @Operation(summary = "Gets tournament edition based on slug.")
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
    public Response editionSingleBySlug(
            @Parameter(required = true, example = "world-cup", schema = @Schema(type = SchemaType.STRING))
            @PathParam("tournamentSlug")
            @Pattern(regexp = REGEX_SLUG, message = INVALID_SLUG)
            String tournamentSlug,
            @Parameter(required = true, example = "qatar-2022", schema = @Schema(type = SchemaType.STRING))
            @PathParam("editionSlug")
            @Pattern(regexp = REGEX_SLUG, message = INVALID_SLUG)
            String editionSlug
    ) {
        TournamentEdition tournamentEdition = this.tournamentEditionService.getTournamentBySlug(tournamentSlug, editionSlug);
        return tournamentEdition != null ? Response.ok().entity(tournamentEdition).build() : Response.noContent().build();
    }

    @GET
    @Path(PATH_EDITION_SINGLE_ID)
    @Operation(summary = DESC_EDITION_SINGLE_ID)
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
    public Response editionSingleById(
            @Parameter(description = DESCRIPTION_ID, required = true, example = "1", schema = @Schema(type = SchemaType.INTEGER))
            @PathParam("id")
            int id
    ) {
        TournamentEdition tournamentEdition = this.tournamentEditionService.getTournamentById(id);
        return Response.ok().entity(tournamentEdition).build();
    }

    @GET
    @Path("{tournamentSlug}/editions/{editionSlug}/matches")
    @Authenticated
    @Operation(summary = "Gets matches of a given tournament edition.")
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
    public Response editionMatchesBySlug(
            @Parameter(required = true, example = "world-cup", schema = @Schema(type = SchemaType.STRING))
            @PathParam("tournamentSlug")
            @Pattern(regexp = REGEX_SLUG, message = INVALID_SLUG)
            String tournamentSlug,
            @Parameter(required = true, example = "qatar-2022", schema = @Schema(type = SchemaType.STRING))
            @PathParam("editionSlug")
            @Pattern(regexp = REGEX_SLUG, message = INVALID_SLUG)
            String editionSlug,
            @Parameter(description = "Next x matches in this tournamentEdition.", example = "4", schema = @Schema(type = SchemaType.INTEGER))
            @QueryParam("next")
            int next
    ) {
        String userId = keycloakService.getKeycloakIdFromMail(sec.getUserPrincipal().getName());
        TournamentEdition tournamentEdition = this.tournamentEditionService.getTournamentBySlug(tournamentSlug, editionSlug);
        betService.updateBetsForTournamentEdition(tournamentEdition, userId);
        List<Match> matches = tournamentEdition.getMatches();
        if (next == 0) {
            return Response.ok().entity(matches).build();
        } else {
            List<Match> nextMatches = matches.stream().filter(m -> m.getKickoff().after(new Date())).collect(Collectors.toList());
            int lastIndex = next > nextMatches.size() ? nextMatches.size() : next;
            return Response.ok().entity(nextMatches.subList(0, lastIndex)).build();
        }
        //return tournamentEdition != null ? Response.ok().entity(tournamentEdition.getMatches()).build() : Response.noContent().build();
    }

    @GET
    @Path("{tournamentSlug}/editions/{editionSlug}/matches/live")
    @Authenticated
    @Operation(summary = "Gets live matches of a given tournament edition.")
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
    public Response editionLiveMatchesBySlug(
            @Parameter(required = true, example = "world-cup", schema = @Schema(type = SchemaType.STRING))
            @PathParam("tournamentSlug")
            @Pattern(regexp = REGEX_SLUG, message = INVALID_SLUG)
            String tournamentSlug,
            @Parameter(required = true, example = "qatar-2022", schema = @Schema(type = SchemaType.STRING))
            @PathParam("editionSlug")
            @Pattern(regexp = REGEX_SLUG, message = INVALID_SLUG)
            String editionSlug
    ) {
        String userId = keycloakService.getKeycloakIdFromMail(sec.getUserPrincipal().getName());
        TournamentEdition tournamentEdition = this.tournamentEditionService.getTournamentBySlug(tournamentSlug, editionSlug);
        betService.updateBetsForTournamentEdition(tournamentEdition, userId);
        List<Match> liveMatches = tournamentEdition.getMatches().stream().filter(Match::isLive).collect(Collectors.toList());
        return Response.ok().entity(liveMatches).build();
    }

    @GET
    @Path("{tournamentSlug}/editions/id/{id}/matches")
    @Authenticated
    @Operation(summary = "Gets matches of tournament edition.")
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
    public Response editionMatchesById(
            @Parameter(description = DESCRIPTION_ID, required = true, example = "1", schema = @Schema(type = SchemaType.INTEGER))
            @PathParam("id")
            int id,
            @Parameter(description = "Next x matches in this tournamentEdition.", example = "4", schema = @Schema(type = SchemaType.INTEGER))
            @QueryParam("next")
            int next
    ) {
        TournamentEdition tournamentEdition = tournamentEditionService.getTournamentById(id);
        String userId = keycloakService.getKeycloakIdFromMail(sec.getUserPrincipal().getName());
        betService.updateBetsForTournamentEdition(tournamentEdition, userId);
        List<Match> matches = tournamentEdition.getMatches();
        matches.sort(Comparator.comparing(Match::getKickoff));
        if (next == 0) {
            return Response.ok().entity(matches).build();
        } else {
            List<Match> nextMatches = matches.stream().filter(m -> m.getKickoff().after(new Date())).collect(Collectors.toList()).subList(0, next);
            nextMatches.sort(Comparator.comparing(Match::getKickoff));
            log.info("Returning next " + nextMatches.size() + " matches. Should be " + next);
            return Response.ok().entity(nextMatches).build();
        }
    }

    @GET
    @Path(PATH_EDITION_GROUPS_SLUG)
    @Operation(summary = DESC_EDITION_GROUPS)
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
    public Response editionGroupsBySlug(
            @Parameter(required = true, example = "world-cup", schema = @Schema(type = SchemaType.STRING))
            @PathParam("tournamentSlug")
            @Pattern(regexp = REGEX_SLUG, message = INVALID_SLUG)
            String tournamentSlug,
            @Parameter(required = true, example = "qatar-2022", schema = @Schema(type = SchemaType.STRING))
            @PathParam("editionSlug")
            @Pattern(regexp = REGEX_SLUG, message = INVALID_SLUG)
            String editionSlug
    ) {
        TournamentEdition tournamentEdition = this.tournamentEditionService.getTournamentBySlug(tournamentSlug, editionSlug);
        return tournamentEdition != null ? Response.ok().entity(tournamentEdition.getGroupsRaw()).build() : Response.noContent().build();
    }

    @GET
    @Path(PATH_EDITION_GROUPS_ID)
    @Operation(summary = DESC_EDITION_GROUPS)
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
    public Response editionGroupsById(
            @Parameter(description = DESCRIPTION_ID, required = true, example = "1", schema = @Schema(type = SchemaType.INTEGER))
            @PathParam("id")
            int id
    ) {
        TournamentEdition tournamentEdition = this.tournamentEditionService.getTournamentById(id);
        return Response.ok().entity(tournamentEdition.getGroupsRaw()).build();
    }

    @GET
    @Path("{tournamentSlug}/editions/{editionSlug}/ranking")
    //@Authenticated
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
    public Response rankingBySlug(
            @Parameter(required = true, example = "world-cup", schema = @Schema(type = SchemaType.STRING))
            @PathParam("tournamentSlug")
            @Pattern(regexp = REGEX_SLUG, message = INVALID_SLUG)
            String tournamentSlug,
            @Parameter(required = true, example = "qatar-2022", schema = @Schema(type = SchemaType.STRING))
            @PathParam("editionSlug")
            @Pattern(regexp = REGEX_SLUG, message = INVALID_SLUG)
            String editionSlug
    ) {
        TournamentEdition tournamentEdition = this.tournamentEditionService.getTournamentBySlug(tournamentSlug, editionSlug);
        List<Ranking> ranking = rankingService.rankingOf(tournamentEdition);
        return ranking != null ? Response.ok().entity(ranking).build() : Response.noContent().build();
    }

    @GET
    @Path("{tournamentSlug}/editions/{editionSlug}/ranking/position")
    @Authenticated
    @Operation(summary = "Get position for user in ranking")
    @APIResponse(
            responseCode = "200",
            description = "OK",
            content = @Content(mediaType = "application/json"))
    public Response getRanking(
            @Parameter(required = true, example = "world-cup", schema = @Schema(type = SchemaType.STRING))
            @PathParam("tournamentSlug")
            @Pattern(regexp = REGEX_SLUG, message = INVALID_SLUG)
            String tournamentSlug,
            @Parameter(required = true, example = "qatar-2022", schema = @Schema(type = SchemaType.STRING))
            @PathParam("editionSlug")
            @Pattern(regexp = REGEX_SLUG, message = INVALID_SLUG)
            String editionSlug
    ) throws IOException {
        TournamentEdition tournamentEdition = this.tournamentEditionService.getTournamentBySlug(tournamentSlug, editionSlug);
        KeycloakUser user = keycloakService.findByMail(sec.getUserPrincipal().getName());

        return Response.ok(rankingService.userRank(user, tournamentEdition)).build();
    }
}
