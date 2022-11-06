package io.easyware.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.easyware.shared.keycloak.KeycloakUser;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.java.Log;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Log
public class Ranking {

    @JsonIgnoreProperties({"createdTimestamp", "username", "enabled", "totp", "emailVerified", "email", "federatedIdentities", "attributes"})
    private KeycloakUser user;

    private List<Bet> bets;

    private int points = 0;
}
