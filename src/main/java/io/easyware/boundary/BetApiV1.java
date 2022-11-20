package io.easyware.boundary;

import io.easyware.entities.Bet;
import io.easyware.entities.Match;
import io.easyware.services.PointsService;
import io.easyware.shared.keycloak.KeycloakUser;
import io.quarkus.security.Authenticated;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.jboss.resteasy.annotations.jaxrs.PathParam;

import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Path("v1/bets")
public class BetApiV1 extends BetApi {

    @GET
    @Path("normalize")
    @Transactional
    public Response normalize() {
        String guids = "414dd9f4-ba7e-40aa-9235-bba930f747c5,3829b1ea-efa4-47fb-899b-14e3047ff243,b8c71c84-1b85-40f8-9d0f-1553914954c6,ff1bd8e3-60a2-4808-9d3c-b453be18ba13,48a3943e-d56d-4483-ade9-195a13fc4d5a,f02f231c-b637-4ee6-8e6d-da5e4646e6ff,d5ad9086-4292-49aa-a2b4-8967d455d364,50f800a5-f875-4fcf-9c29-13e037683df4,5dd6c1f9-fc52-4120-a087-5c6deb23279e,5dc7fb1c-3226-44ae-b283-987c61ae58b4,0037ba88-e43d-4596-b922-bf8c468c5e20,2ece5d1b-0851-4570-8a57-33251c0596aa,f47b9b5f-b37c-475f-96aa-578441b1f0aa,c2e02e89-eb4f-4fb8-9aea-6f32aabdef70,0944494c-b2a6-4830-a4d4-6681dc1c80c3,1bb5a8e5-32b7-4ad9-826c-a72938f3f493,5a3ae718-3405-4747-a051-0af3d80c8c9b,5154362c-a973-4953-8826-aee53c458acf,0a602b62-5ad0-4731-8d55-84ecee3e0550,f7f0db5c-4d93-4129-b0e7-9ae568938f84,ab513eb3-d692-4c15-84b5-4df9bc54fa1a,855787e8-da64-45f1-855e-7a590452755e,c1ce2330-6677-4d64-95cf-25cf723dad61,27e5dc7a-dd54-4cf5-9a84-f3513909600c,a4c667b5-1cde-4df4-977e-3a3ee34062e2";
        List<String> listGuids = Arrays.asList(guids.split(","));

        listGuids.forEach(guid -> {


            for (int i = 0; i <= 64; i++) {

                int finalI = i;
                log.info("Disabling bets of " + guid + " in match " + finalI);
                betService.disableBetsFromUser(guid, finalI);
                log.info("Disable complete.");
                List<Bet> betsOfMatch = betService.getAllBetsFromUserForMatch(guid, finalI);
                log.info("UserId: " + guid + " -> Match: " + finalI + " -> Bets found: " + betsOfMatch.size());
                if (betsOfMatch.size() > 0) {
                    betsOfMatch.sort(Comparator.comparing(Bet::getBetPlacedOn).reversed());
                    Bet bet = betsOfMatch.get(0);
                    log.info("  Updating bet " + bet.getId() + ". Current " + bet.isActive());
                    bet.setActive(true);
                    log.info("  Updated bet " + bet.getId() + ". Current " + bet.isActive());
                    bet.persistAndFlush();
                    log.info("  Saved bet " + bet.toString() + ".");
                }
            }
        });

        betService.findAll().stream().filter(bet -> bet.getMatchId() == 1).forEach(bet -> log.info(bet.toString()));
        return Response.ok().build();
    }

    @POST
    @Authenticated
    @Operation(summary = "Save bet")
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
    public Response create(@Valid Bet bet) throws URISyntaxException, IOException {
        KeycloakUser user = keycloakService.findByMail(sec.getUserPrincipal().getName());
        bet.setUserId(user.getId());
        bet.setBetPlacedOn(new Date());

        Match match = matchService.find(bet.getMatchId());

        if (match.getKickoff().after(new Date())) {
            betService.disableBetsFromUser(user.getId(), match.getId());

            Bet createdBet = betService.save(bet);
            URI location = new URI("v1/bets/" + createdBet.getId());
            return Response.created(location).entity(createdBet).build();
        } else {
            return Response.notModified().build();
        }
    }

    @GET
    @Path("matches/{matchId}")
    @Authenticated
    @Operation(summary = "List all bets")
    @APIResponse(
            responseCode = "400",
            description = "Bad request",
            content = @Content(mediaType = "application/json"))
    @APIResponse(
            responseCode = "200",
            description = "OK",
            content = @Content(mediaType = "application/json"))
    @APIResponse(
            responseCode = "204",
            description = "No content",
            content = @Content(mediaType = "application/json"))
    public Response list(@PathParam long matchId) {
        Match match = matchService.find(matchId);

        if (match.getKickoff().before(new Date())) {
            List<Bet> allBets = betService.findValidBetsOfMatch(match);
            if (allBets == null) return Response.noContent().build();
            log.info("Bets found: " + allBets.size());
            allBets.stream().forEach(bet -> {
                Integer points = PointsService.calculatePoints(match, bet);
                bet.setPoints(points);

                KeycloakUser user = null;
                try {
                    user = keycloakService.findUserByKeycloakId(bet.getUserId());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                bet.setUser(user);
            });
            return Response.ok().entity(allBets).build();
        } else {
            return Response.noContent().build();
        }


    }


}
