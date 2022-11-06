package io.easyware.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.java.Log;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Log
@Table(name = "tournament_group")
public class TournamentGroup {

    @Id
    @NotBlank
    private long id;

    @NotBlank
    @Column(name = "tournament_edition_id")
    @JsonIgnore
    private long tournamentEditionId;

    @NotBlank
    private String name;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name="group_id")
    @JsonIgnoreProperties({"id", "groupId", "order"})
    private List<TournamentGroupMember> members;
}
