package io.easyware.services;

import io.easyware.entities.Team;
import io.easyware.entities.Tournament;
import io.easyware.repositories.TournamentRepository;
import io.easyware.shared.Slug;
import io.easyware.shared.http.HttpClient;
import io.easyware.shared.http.ParameterStringBuilder;
import org.jose4j.json.internal.json_simple.JSONObject;
import org.jose4j.json.internal.json_simple.parser.JSONParser;
import org.jose4j.json.internal.json_simple.parser.ParseException;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

@ApplicationScoped
public class TournamentService implements Slug {

    @Inject
    TeamsService teamsService;

    private final String TOKEN = "1a8ebe538b1d4c71a2ea2e30e518236e";

    private final TournamentRepository tournamentRepository;

    TournamentService(TournamentRepository tournamentRepository) {
        this.tournamentRepository = tournamentRepository;
    }

    public List<Tournament> getAllTournaments() { return tournamentRepository.listAll(); }

    public String idToSlug(long tournamentId) {
        return slugify(tournamentRepository.findById(tournamentId).getName());
    }

    public void getId() throws IOException, ParseException {
        URL url = new URL("https://api.football-data.org//v4/competitions/WC/matches");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("X-Auth-Token", TOKEN);
        con.setDoOutput(true);
        con.connect();
        InputStream inputStream = con.getInputStream();
        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject = (JSONObject)jsonParser.parse(new InputStreamReader(inputStream, "UTF-8"));

        List<JSONObject> matches = (List<JSONObject>) jsonObject.get("matches");

        matches.forEach(match -> {
            Long id = (Long) match.get("id");
            JSONObject home = (JSONObject) match.get("homeTeam");
            JSONObject away = (JSONObject) match.get("awayTeam");
            try {
                Team homeTeam = teamsService.getTeamByShortName((String) home.get("tla"));
                Team awayTeam = teamsService.getTeamByShortName((String) away.get("tla"));
                System.out.println(
                        "-- " + homeTeam.getShortName() + " x " + awayTeam.getShortName() + "\n" +
                                "UPDATE matches SET api_id = '" + id + "' WHERE team_a=" + homeTeam.getId() + " AND team_b=" + awayTeam.getId());
            } catch (Exception e) {
                System.out.println("-- FAILED! " + home.get("tla") + " x " + away.get("tla"));
            }

        });
    }
}
