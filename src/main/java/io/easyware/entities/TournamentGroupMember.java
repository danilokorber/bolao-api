package io.easyware.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.java.Log;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Log
@Table(name = "group_member")
public class TournamentGroupMember {

    @Id
    @NotBlank
    private long id;

    @NotBlank
    @Column(name = "group_id")
    private long groupId;

    @NotBlank
    @Column(name = "group_order")
    private int order;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name="team_id")
    private Team team;

}
