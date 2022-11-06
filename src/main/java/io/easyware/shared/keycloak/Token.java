package io.easyware.shared.keycloak;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.java.Log;

import java.util.Calendar;
import java.util.Date;

@Log
@Getter
@Setter
public class Token {
    @JsonProperty("access_token") private String accessToken;
    @JsonProperty("expires_in") private int expiresIn;
    @JsonProperty("refresh_expires_in") private int refreshExpiresIn;
    @JsonProperty("refresh_token") private String refreshToken;
    @JsonProperty("token_type") private String tokenType;
    @JsonProperty("not-before-policy") private int notBeforePolicy;
    @JsonProperty("session_state") private String sessionState;
    @JsonProperty("scope") private String scope;
    private Date createdAt;
    private boolean valid;

    public Token() {
        Calendar cal = Calendar.getInstance();
        this.createdAt = cal.getTime();
    }

    public boolean isValid() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(createdAt);
        cal.add(Calendar.SECOND, expiresIn);
        Date now = new Date();
        return now.before(cal.getTime());
    }
}
