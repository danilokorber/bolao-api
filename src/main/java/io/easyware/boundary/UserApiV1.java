package io.easyware.boundary;

import io.easyware.entities.BonusBet;
import io.easyware.enums.AuthenticationStep;
import io.easyware.shared.keycloak.KeycloakUser;
import io.easyware.shared.keycloak.KeycloakUserAttributes;
import io.easyware.shared.keycloak.KeycloakUserWithoutPicture;
import io.quarkus.security.Authenticated;
import lombok.extern.java.Log;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import javax.validation.Valid;
import javax.validation.constraints.Pattern;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Log
@Path("v1/users")
@Tag(name = "Users")
public class UserApiV1 extends UserApi {

    private final String REGEX_MAIL = "^(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])$";
    private final String REGEX_GUID = "^[{]?[0-9a-fA-F]{8}-([0-9a-fA-F]{4}-){3}[0-9a-fA-F]{12}[}]?$";

    @GET
    @Path("profile")
    @Authenticated
    @Operation(summary = "Get authenticated user profile")
    @APIResponse(
            responseCode = "200",
            description = "OK",
            content = @Content(mediaType = "application/json"))
    public Response getProfile() {
        String mail = sec.getUserPrincipal().getName();
        return getProfileByMail(mail);
    }

    @GET
    @Path("profile/{mail}")
    @Authenticated
    @Operation(summary = "Gets user profile based on a given email")
    @APIResponse(
            responseCode = "200",
            description = "OK",
            content = @Content(mediaType = "application/json"))
    @APIResponse(
            responseCode = "204",
            description = "No content",
            content = @Content(mediaType = "application/json"))
    @APIResponse(
            responseCode = "400",
            description = "Bad request",
            content = @Content(mediaType = "application/json"))
    public Response getProfileByMail(
            @Parameter(description = "Mail address of a given user", required = true, example = "mail@domain.com", schema = @Schema(type = SchemaType.STRING))
            @PathParam("mail")
            @Pattern(regexp = REGEX_MAIL, message = "Invalid mail address.")
            String mail
    ) {
        try {
            keycloakService.updateUserList();
            KeycloakUser user = keycloakService.findByMail(mail);
            return user == null ? Response.noContent().build() : Response.ok().entity(user).build();
        } catch (Exception e) {
            return Response.serverError().entity(e).build();
        }
    }

    @PUT
    @Path("guid/{guid}/accept-rules")
    @Authenticated
    @Operation(summary = "User accepts rules")
    @APIResponse(
            responseCode = "200",
            description = "OK",
            content = @Content(mediaType = "application/json"))
    @APIResponse(
            responseCode = "204",
            description = "No content",
            content = @Content(mediaType = "application/json"))
    @APIResponse(
            responseCode = "400",
            description = "Bad request",
            content = @Content(mediaType = "application/json"))
    public Response acceptRules(
            @Parameter(description = "Guid of a given user", required = true, example = "aaaaaaaa-bbbb-cccc-eeee-ffffffffffff", schema = @Schema(type = SchemaType.STRING))
            @PathParam("guid")
            @Pattern(regexp = REGEX_GUID, message = "Invalid guid.")
            String guid
    ) {
        try {
            KeycloakUser authenticatedUser = keycloakService.findByMail(sec.getUserPrincipal().getName());
            if (authenticatedUser.getId().equals(guid)) {
                return Response.ok().entity(updateAuthenticationStep(guid, AuthenticationStep.ACCEPT_RULES)).build();
            } else {
                return Response.status(Response.Status.FORBIDDEN).build();
            }
        } catch (Exception e) {
            return Response.serverError().entity(e).build();
        }
    }

    @PUT
    @Path("guid/{guid}/data-privacy")
    @Authenticated
    @Operation(summary = "User accepts data privacy")
    @APIResponse(
            responseCode = "200",
            description = "OK",
            content = @Content(mediaType = "application/json"))
    @APIResponse(
            responseCode = "204",
            description = "No content",
            content = @Content(mediaType = "application/json"))
    @APIResponse(
            responseCode = "400",
            description = "Bad request",
            content = @Content(mediaType = "application/json"))
    public Response acceptDataPrivacy(
            @Parameter(description = "Guid of a given user", required = true, example = "aaaaaaaa-bbbb-cccc-eeee-ffffffffffff", schema = @Schema(type = SchemaType.STRING))
            @PathParam("guid")
            @Pattern(regexp = REGEX_GUID, message = "Invalid guid.")
            String guid
    ) {
        try {
            KeycloakUser authenticatedUser = keycloakService.findByMail(sec.getUserPrincipal().getName());
            if (authenticatedUser.getId().equals(guid)) {
                return Response.ok().entity(updateAuthenticationStep(guid, AuthenticationStep.ACCEPT_DATA_PRIVACY)).build();
            } else {
                return Response.status(Response.Status.FORBIDDEN).build();
            }
        } catch (Exception e) {
            return Response.serverError().entity(e).build();
        }
    }

    @PUT
    @Path("guid/{guid}/preferences")
    @Authenticated
    @Operation(summary = "User sets locale")
    @APIResponse(
            responseCode = "200",
            description = "OK",
            content = @Content(mediaType = "application/json"))
    @APIResponse(
            responseCode = "204",
            description = "No content",
            content = @Content(mediaType = "application/json"))
    @APIResponse(
            responseCode = "400",
            description = "Bad request",
            content = @Content(mediaType = "application/json"))
    public Response setLocale(
            @Parameter(description = "Guid of a given user", required = true, example = "aaaaaaaa-bbbb-cccc-eeee-ffffffffffff", schema = @Schema(type = SchemaType.STRING))
            @PathParam("guid")
            @Pattern(regexp = REGEX_GUID, message = "Invalid guid.")
            String guid,
            KeycloakUser user
    ) {
        try {
            KeycloakUser authenticatedUser = keycloakService.findByMail(sec.getUserPrincipal().getName());
            ArrayList<String> locales = (ArrayList<String>) user.getAttributes().getLocale();
            if (authenticatedUser.getId().equals(guid)) {
                return Response.ok().entity(updateAuthenticationStep(guid, AuthenticationStep.LOCALE, locales)).build();
            } else {
                return Response.status(Response.Status.FORBIDDEN).build();
            }
        } catch (Exception e) {
            return Response.serverError().entity(e).build();
        }
    }

    @PUT
    @Path("guid/{guid}/payment")
    @Authenticated
    @Operation(summary = "User makes payment")
    @APIResponse(
            responseCode = "200",
            description = "OK",
            content = @Content(mediaType = "application/json"))
    @APIResponse(
            responseCode = "204",
            description = "No content",
            content = @Content(mediaType = "application/json"))
    @APIResponse(
            responseCode = "400",
            description = "Bad request",
            content = @Content(mediaType = "application/json"))
    public Response acceptPayment(
            @Parameter(description = "Guid of a given user", required = true, example = "aaaaaaaa-bbbb-cccc-eeee-ffffffffffff", schema = @Schema(type = SchemaType.STRING))
            @PathParam("guid")
            @Pattern(regexp = REGEX_GUID, message = "Invalid guid.")
            String guid
    ) {
        try {
            KeycloakUser authenticatedUser = keycloakService.findByMail(sec.getUserPrincipal().getName());
            if (authenticatedUser.getId().equals(guid)) {
                return Response.ok().entity(updateAuthenticationStep(guid, AuthenticationStep.CONFIRM_PAYMENT)).build();
            } else {
                return Response.status(Response.Status.FORBIDDEN).build();
            }
        } catch (Exception e) {
            return Response.serverError().entity(e).build();
        }
    }

    @PUT
    @Authenticated
    @Path("guid/{guid}/bonus")
    @Operation(summary = "Save bonus bets")
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
    @APIResponse(
            responseCode = "201",
            description = "Created",
            content = @Content(mediaType = "application/json"))
    @Consumes(MediaType.APPLICATION_JSON)
    public Response bonus(@Parameter(description = "Guid of a given user", required = true, example = "aaaaaaaa-bbbb-cccc-eeee-ffffffffffff", schema = @Schema(type = SchemaType.STRING))
                          @PathParam("guid")
                          @Pattern(regexp = REGEX_GUID, message = "Invalid guid.")
                          String guid, @Valid BonusBet bonusBets) throws URISyntaxException, IOException {
        KeycloakUser authenticatedUser = keycloakService.findByMail(sec.getUserPrincipal().getName());

        if (new Date().before(new Date(2022, 10, 25, 10, 00)) && authenticatedUser.getId().equals(guid)) {
            return Response.ok().entity(updateAuthenticationStep(guid, AuthenticationStep.BONUS_BETS, bonusBets)).build();
        } else {
            return Response.status(Response.Status.FORBIDDEN).build();
        }
    }

    private KeycloakUser updateAuthenticationStep(String guid, AuthenticationStep step) throws Exception {
        return updateAuthenticationStep(guid, step, new ArrayList<String>());
    }

    private KeycloakUser updateAuthenticationStep(String guid, AuthenticationStep step, List<String> attribute) throws IOException {
        KeycloakUser user = keycloakService.findUserByKeycloakId(guid);
        KeycloakUserAttributes attributes = user.getAttributes();
        List<Date> list = new ArrayList<>();
        list.add(new Date());

        switch (step) {
            case ACCEPT_RULES:
                attributes.setRules_accepted_on(list);
                break;
            case ACCEPT_DATA_PRIVACY:
                attributes.setData_privacy_accepted_on(list);
                break;
            case CONFIRM_PAYMENT:
                attributes.setPayment_on(list);
                break;
            case LOCALE:
                attributes.setLocale(attribute);
                break;
            default:
        }
        KeycloakUserWithoutPicture userWithoutPicture = new KeycloakUserWithoutPicture(user);
        KeycloakUser updatedUser = keycloakService.persist(userWithoutPicture);
        log.info("New: " + updatedUser.toString());
        keycloakService.updateUserList();
        return updatedUser;
    }

    private KeycloakUser updateAuthenticationStep(String guid, AuthenticationStep step, BonusBet bonusBet) throws IOException {
        KeycloakUser user = keycloakService.findUserByKeycloakId(guid);

        switch (step) {
            case BONUS_BETS:
                user.getAttributes().setFirst(bonusBet.getFirst());
                user.getAttributes().setSecond(bonusBet.getSecond());
                user.getAttributes().setThird(bonusBet.getThird());
                user.getAttributes().setFourth(bonusBet.getFourth());
                break;
            default:
        }
        KeycloakUserWithoutPicture userWithoutPicture = new KeycloakUserWithoutPicture(user);
        KeycloakUser updatedUser = keycloakService.persist(userWithoutPicture);
        log.info("New: " + updatedUser.toString());
        keycloakService.updateUserList();
        return updatedUser;
    }
}
