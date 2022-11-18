package io.easyware.services;

import io.easyware.entities.Team;
import io.easyware.repositories.TeamsRepository;

import javax.enterprise.context.ApplicationScoped;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class TeamsService {

    private final TeamsRepository teamsRepository;

    public TeamsService(TeamsRepository teamsRepository) {
        this.teamsRepository = teamsRepository;
    }

    public List<Team> getAllTeams() {
        return this.teamsRepository.listAll().stream().filter(t -> t.getId() <= 32).collect(Collectors.toList());
    }

    public List<Team> getAllTeamsFromGroup(String group) {
        return this.teamsRepository.find("group", group.toUpperCase()).list();
    }

    public Team getTeamById(String teamId) {
        return this.teamsRepository.find("id", teamId.toUpperCase()).firstResult();
    }

    public Team getTeamByShortName(String shortName) {
        return this.teamsRepository.find("short_name", shortName.toUpperCase()).firstResult();
    }
}
