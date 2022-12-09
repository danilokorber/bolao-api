package io.easyware.boundary;

import io.easyware.entities.Bet;
import io.easyware.entities.Match;
import io.easyware.entities.Survey;
import io.easyware.shared.keycloak.KeycloakUser;
import io.quarkus.security.Authenticated;
import lombok.Getter;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;

import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Path("v1/surveys")
public class SurveyApiV1 extends SurveyApi {

    @GET
    @Authenticated
    @Operation(summary = "Get answers from authenticated user")
    @APIResponse(
            responseCode = "400",
            description = "Bad request",
            content = @Content(mediaType = "application/json"))
    @APIResponse(
            responseCode = "200",
            description = "OK",
            content = @Content(mediaType = "application/json"))
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getMine() {
        KeycloakUser authenticatedUser = keycloakService.findByMail(sec.getUserPrincipal().getName());
        try {
            List<Survey> myAnswers = surveyService.getAnswersFromUser(authenticatedUser);
            return Response.ok().entity(myAnswers).build();
        } catch (Exception e) {
            return Response.notModified().build();
        }
    }

    @PUT
    @Authenticated
    @Operation(summary = "Save survey")
    @APIResponse(
            responseCode = "400",
            description = "Bad request",
            content = @Content(mediaType = "application/json"))
    @APIResponse(
            responseCode = "304",
            description = "Not modified",
            content = @Content(mediaType = "application/json"))
    @APIResponse(
            responseCode = "200",
            description = "OK",
            content = @Content(mediaType = "application/json"))
    @Consumes(MediaType.APPLICATION_JSON)
    public Response save(@Valid Survey survey) {
        KeycloakUser authenticatedUser = keycloakService.findByMail(sec.getUserPrincipal().getName());
        survey.setUserId(authenticatedUser.getId());
        log.info(survey.toString());
        try {
            surveyService.saveAnswer(survey);
            return Response.ok().entity(survey).build();
        } catch (Exception e) {
            log.severe(e.getMessage());
            return Response.notModified().entity(survey).build();
        }
    }
}
