package io.easyware.boundary;

import io.easyware.entities.Bet;
import io.easyware.entities.Ranking;
import io.easyware.entities.TournamentEdition;
import io.easyware.shared.keycloak.KeycloakUser;
import io.quarkus.security.Authenticated;
import lombok.extern.java.Log;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicInteger;

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
    @Authenticated
    @Path("history")
    public Response history() throws ParseException {
        KeycloakUser user = keycloakService.findByMail(sec.getUserPrincipal().getName());
        TournamentEdition tournamentEdition = this.tournamentEditionService.getTournamentById(1);

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date toDelete = formatter.parse("2022-11-19");
        Date startDate = formatter.parse("2022-11-20");
        Date endDate = new Date();
        LocalDate delete = toDelete.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate start = startDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate end = endDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().plusDays(1);

        Map<LocalDate, AtomicInteger> history = new HashMap<>();

        for (LocalDate date = start; date.isBefore(end) || date.isEqual(end); date = date.plusDays(1)) {
            List<Bet> bets = betService.getAllBetsFromUserForTournamentEdition(user.getId(), tournamentEdition, Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant()));
            AtomicInteger points = new AtomicInteger();
            bets.forEach(bet -> points.addAndGet(bet.getPoints()));
            history.put(date.minusDays(1), points);
        }

        Map<LocalDate, AtomicInteger> sortedHistory = new TreeMap<LocalDate, AtomicInteger>(history);
        sortedHistory.remove(delete);

        return Response.ok().entity(sortedHistory).build();
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
