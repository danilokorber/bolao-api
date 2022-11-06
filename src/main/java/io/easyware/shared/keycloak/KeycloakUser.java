package io.easyware.shared.keycloak;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import lombok.extern.java.Log;

import java.util.List;
import java.util.Objects;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Log
@JsonIgnoreProperties(ignoreUnknown = true)
public class KeycloakUser {
    private String id;
    private long createdTimestamp;
    private String username;
    private boolean enabled;
    private boolean totp;
    private boolean emailVerified;
    private String firstName;
    private String lastName;
    private String email;
    private KeycloakUserAttributes attributes;
    private List<Identity> federatedIdentities;

    private String picture;

    public String getIdentityProviderId(String identityProvider) {
        try {
        Identity selectedIdentity = this.federatedIdentities.stream().filter(i -> Objects.equals(i.identityProvider, identityProvider)).findFirst().get();
            return selectedIdentity.getUserId();
        } catch (Exception e) {
            log.warning(identityProvider + " is not a valid identity provider for " + this.username)  ;
            return null;
        }
    }

    public String getPicture() {
        try {
            if (attributes.getPicture().size() > 0) {
                return attributes.getPicture().get(0);
            }
            return "";
        } catch (Exception e) {
            return "";
        }
    }
}