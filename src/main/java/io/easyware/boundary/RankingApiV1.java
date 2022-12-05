package io.easyware.boundary;

import io.easyware.entities.Bet;
import io.easyware.entities.Ranking;
import io.easyware.entities.TournamentEdition;
import io.easyware.shared.keycloak.KeycloakUser;
import io.quarkus.security.Authenticated;
import io.vertx.core.json.JsonObject;
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
    @Path("history1")
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
    //@Authenticated
    @Path("history")
    public Response history2() throws ParseException, IOException {
        KeycloakUser user = keycloakService.findByMail(sec.getUserPrincipal().getName());
        TournamentEdition tournamentEdition = this.tournamentEditionService.getTournamentById(1);

        List<Ranking> ranking = rankingService.prepareFor(tournamentEdition);
        Ranking firstPlace = ranking.get(0);
        KeycloakUser firstUser = keycloakService.findByMail(firstPlace.getUser().getEmail());

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date toDelete = formatter.parse("2022-11-19");
        Date startDate = formatter.parse("2022-11-20");
        Date endDate = new Date();
        LocalDate delete = toDelete.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate start = startDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate end = endDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().plusDays(1);

        Map<LocalDate, AtomicInteger> myHistory = new HashMap<>();
        Map<LocalDate, AtomicInteger> firstHistory = new HashMap<>();
        Map<LocalDate, Integer> myPositions = new HashMap<>();
        Map<LocalDate, Integer> firstPositions = new HashMap<>();

        for (LocalDate date = start; date.isBefore(end) || date.isEqual(end); date = date.plusDays(1)) {
            List<Bet> myBets = betService.getAllBetsFromUserForTournamentEdition(user.getId(), tournamentEdition, Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant()));
            AtomicInteger myPoints = new AtomicInteger();
            myBets.forEach(bet -> myPoints.addAndGet(bet.getPoints()));
            myHistory.put(date.minusDays(1), myPoints);
            myPositions.put(date.minusDays(1), (Integer) rankingService.userRank(user, tournamentEdition, Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant())).get("position"));

            List<Bet> firstBets = betService.getAllBetsFromUserForTournamentEdition(firstUser.getId(), tournamentEdition, Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant()));
            AtomicInteger firstPoints = new AtomicInteger();
            firstBets.forEach(bet -> firstPoints.addAndGet(bet.getPoints()));
            firstHistory.put(date.minusDays(1), firstPoints);
            firstPositions.put(date.minusDays(1), (Integer) rankingService.userRank(firstUser, tournamentEdition, Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant())).get("position"));

        }

        Map<LocalDate, AtomicInteger> sortedMyHistory = new TreeMap<LocalDate, AtomicInteger>(myHistory);
        Map<LocalDate, Integer> sortedMyPositions = new TreeMap<LocalDate, Integer>(myPositions);
        sortedMyHistory.remove(delete);
        sortedMyPositions.remove(delete);
        JsonObject my = new JsonObject();
        my.put("position", sortedMyPositions);
        my.put("history", sortedMyHistory);

        Map<LocalDate, AtomicInteger> sortedFirstHistory = new TreeMap<LocalDate, AtomicInteger>(firstHistory);
        Map<LocalDate, Integer> sortedFirstPositions = new TreeMap<LocalDate, Integer>(firstPositions);
        sortedFirstHistory.remove(delete);
        sortedFirstPositions.remove(delete);
        JsonObject first = new JsonObject();
        first.put("position", sortedFirstPositions);
        first.put("history", sortedFirstHistory);

        JsonObject history = new JsonObject();
        history.put("first", first);
        history.put("my", my);

        return Response.ok().entity(history).build();
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
