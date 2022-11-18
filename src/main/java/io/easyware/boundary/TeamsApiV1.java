package io.easyware.boundary;

import io.easyware.entities.Team;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Pattern;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("v1/teams")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class TeamsApiV1 extends TeamsApi {

    private final String REGEX_GROUP = "^([a-hA-H])$";
    private final String INVALID_GROUP = "Invalid group. Allowed values are from A to H.";
    private final String DESCRIPTION_GROUP = "Can be any letter from A to H.";
    private final String REGEX_TEAMID = "^([a-hA-H])([1-4])$";
    private final String INVALID_TEAMID = "Invalid teamId";
    private final String DESCRIPTION_TEAMID = "Combination of group and order.";
    private final String INVALID_ORDER = "Invalid order. Allowed values are between 1 and 4.";
    private final String DESCRIPTION_ORDER = "Can be any number from 1 to 4.";
    private final String PATH_ALL_IN_GROUP = "group/{group}";
    private final String DESC_ALL_IN_GROUP = "Gets all details from the four nations in the given group.";
    private final String PATH_GROUP_AND_ORDER = "group/{group}/order/{order}";
    private final String DESC_GROUP_AND_ORDER = "Gets details of nations in the given group with the given order.";

    private final String DESC_TEAM_BY_ID = "";



    @GET
    @Operation(summary = "List all teams.")
    @APIResponse(
            responseCode = "200",
            description = "OK",
            content = @Content(mediaType = "application/json"))
    public Response all() {
        List<Team> teams = this.teamsService.getAllTeams();
        return Response.ok().entity(teams).build();
    }

    @GET
    @Path("{teamId}")
    @Operation(summary = "Get details of a given team based teamId (group + order).")
    @APIResponse(
            responseCode = "200",
            description = "OK",
            content = @Content(mediaType = "application/json"))
    @APIResponse(
            responseCode = "400",
            description = "Bad request",
            content = @Content(mediaType = "application/json"))
    public Response findByTeamId(
            @Parameter(description = DESCRIPTION_TEAMID, required = true, example = "A1", schema = @Schema(type = SchemaType.STRING))
            @PathParam("teamId")
            @Pattern(regexp = REGEX_TEAMID, message = INVALID_TEAMID)
                    String teamId
    ) {
        Team teams = this.teamsService.getTeamById(teamId);
        return Response.ok().entity(teams).build();
    }

    @GET
    @Path(PATH_ALL_IN_GROUP)
    @Operation(summary = DESC_ALL_IN_GROUP)
    @APIResponse(
            responseCode = "400",
            description = "Bad request",
            content = @Content(mediaType = "application/json"))
    @APIResponse(
            responseCode = "200",
            description = "OK",
            content = @Content(mediaType = "application/json"))
    public Response allInGroup(
            @Parameter(description = DESCRIPTION_GROUP, required = true, example = "A", schema = @Schema(type = SchemaType.STRING))
            @PathParam("group")
            @Pattern(regexp = REGEX_GROUP, message = INVALID_GROUP) String group
    ) {
        List<Team> teams = this.teamsService.getAllTeamsFromGroup(group);
        return Response.ok().entity(teams).build();
    }

    @GET
    @Path(PATH_GROUP_AND_ORDER)
    @Operation(summary = DESC_GROUP_AND_ORDER)
    @APIResponse(
            responseCode = "400",
            description = "Bad request",
            content = @Content(mediaType = "application/json"))
    @APIResponse(
            responseCode = "200",
            description = "OK",
            content = @Content(mediaType = "application/json"))
    public Response oneWithGroupAndOrder(
            @Parameter(description = DESCRIPTION_GROUP, required = true, example = "A", schema = @Schema(type = SchemaType.STRING))
            @PathParam("group")
            @Pattern(regexp = REGEX_GROUP, message = INVALID_GROUP)
                   String group,
            @Parameter(description = DESCRIPTION_ORDER, required = true, example = "1", schema = @Schema(type = SchemaType.NUMBER))
            @PathParam("order")
            @Range(min=1, max=4, message = INVALID_ORDER)
                    int order
    ) {
        return findByTeamId(group + order);
    }

}
