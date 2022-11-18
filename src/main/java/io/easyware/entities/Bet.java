package io.easyware.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.easyware.services.PointsService;
import io.easyware.shared.keycloak.KeycloakUser;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.java.Log;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

import static io.easyware.shared.Slug.BASE_URL;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Log
@Table(name = "bet")
@JsonPropertyOrder({"id", "matchId", "userId", "scoreHome", "scoreAway", "points", "betPlacedOn"})
public class Bet extends PanacheEntityBase {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "id", nullable = false, unique = true, columnDefinition = "BINARY(16)")
    private UUID id;

    @Column(name = "match_id")
    private long matchId;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "score_a")
    private long scoreHome;

    @Column(name = "score_b")
    private long scoreAway;

    @Column(name = "bet_made_at")
    @GeneratedValue
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    private Date betPlacedOn;

    @Transient
    private int points = 0;

    @Transient
    @JsonIgnoreProperties({"createdTimestamp", "username", "enabled", "totp", "emailVerified", "email", "federatedIdentities", "attributes"})
    private KeycloakUser user;

    private boolean active = true;

    @Transient
    private long tournamentEditionId;

    public String getUrl() {
        return BASE_URL + "/bets/id/" + id;
    }

    public int getPoints() { return PointsService.calculatePoints( this);  }
}
