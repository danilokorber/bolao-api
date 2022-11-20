package io.easyware.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.easyware.services.BetService;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.java.Log;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static io.easyware.shared.Slug.BASE_URL;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Log
@Table(name = "matches")
@JsonPropertyOrder({"id", "url", "tournamentMatchId", "groupId", "kickoff", "venue", "home", "away", "scoreHome", "scoreAway", "psoHome", "psoAway"})
public class Match extends PanacheEntityBase {

    @Id
    //@NotBlank
    private long id;

    //@NotBlank
    @Column(name = "tournament_edition_id")
    @JsonIgnore
    private long tournamentEditionId;

    //@NotBlank
    @Column(name = "tournament_match_id")
    private long tournamentMatchId;

    //@NotBlank
    @Column(name = "group_id")
    private long groupId;

    //@NotBlank
    @Column(name = "kick_off")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    private Date kickoff;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="venue_id")
    @JsonIgnoreProperties({"id"})
    private Venue venue;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="team_a")
    @JsonIgnoreProperties({"id"})
    private Team home;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="team_b")
    @JsonIgnoreProperties({"id"})
    private Team away;

    @Column(name = "score_a")
    private Integer scoreHome;

    @Column(name = "score_b")
    private Integer scoreAway;

    @Column(name = "pen_a")
    private Integer psoHome;

    @Column(name = "pen_b")
    private Integer psoAway;

    @Column(name = "api_id")
    private Integer apiId;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name="match_id")
    @JsonIgnoreProperties({"matchId"})
    private List<Bet> bets;

    @Transient
    boolean live;

    public String getUrl() {
        return BASE_URL + "/matches/" + id;
    }

    public void clearBets(long userId) {
        if (this.getKickoff().before(new Date())) {
            setBets(getBets().stream().filter(bet -> bet.isActive()).collect(Collectors.toList()));
        } else {
            setBets(getBets().stream().filter(bet -> bet.isActive() && bet.getUserId().equals(userId)).collect(Collectors.toList()));
        }
    }

    public boolean isLive() {
        Date now = new Date();
        long diff = now.getTime() - kickoff.getTime();

        long minutes = TimeUnit.MILLISECONDS.toMinutes(diff);
        return (minutes >= 0) && (minutes <= 180);
    }
}
