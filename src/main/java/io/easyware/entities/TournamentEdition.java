package io.easyware.entities;

import com.fasterxml.jackson.annotation.*;
import io.easyware.shared.Slug;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.java.Log;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Log
@Table(name = "tournament_edition")
@JsonPropertyOrder({"id", "url", "name", "startDate", "endDate", "groups"})
public class TournamentEdition implements Slug {

    @Id
    @NotBlank
    private long id;

    @NotBlank
    private String name;

    @NotBlank
    @Column(name = "tournament_id")
    @JsonIgnore
    private long tournamentId;

    @NotBlank
    @Column(name = "open_date")
    @Temporal(TemporalType.DATE)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Europe/Berlin")
    private Date startDate;

    @NotBlank
    @Column(name = "close_date")
    @Temporal(TemporalType.DATE)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Europe/Berlin")
    private Date endDate;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name="tournament_edition_id")
    @JsonIgnore
    private List<TournamentGroup> groupsRaw;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name="tournament_edition_id")
    private List<Match> matches;

    public String getUrl() {
        return BASE_URL + "/tournaments/"  + "/editions/" + slugify(name);
    }

    public List<Map<String, Object>> getGroups() {
        List<Map<String, Object>> groups = new ArrayList<>();
        groupsRaw.forEach(group -> addToGroups(groups, group));
        return groups;
    }

    private void addToGroups(List<Map<String, Object>> groups, TournamentGroup tournamentGroup) {
        Map<String, Object> group = new HashMap<>();
        List<Map<String, Object>> members = new ArrayList<>();
        tournamentGroup.getMembers().forEach(member -> addToMembers(members, member));
        group.put("members", members);
        group.put("name", tournamentGroup.getName());
        groups.add(group);
    }

    private void addToMembers(List<Map<String, Object>> members, TournamentGroupMember tournamentGroupMember) {
        Map<String, Object> member = new HashMap<>();
        member.put("shortName", tournamentGroupMember.getTeam().getShortName());
        member.put("en", tournamentGroupMember.getTeam().getEn());
        member.put("de", tournamentGroupMember.getTeam().getDe());
        member.put("pt", tournamentGroupMember.getTeam().getPt());
        member.put("url", BASE_URL + "/teams/id/" + tournamentGroupMember.getTeam().getId());
        members.add(member);
    }
}
