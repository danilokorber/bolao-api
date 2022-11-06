package io.easyware.shared.keycloak;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
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
public class KeycloakUserWithoutPicture {
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

    public String getIdentityProviderId(String identityProvider) {
        try {
        Identity selectedIdentity = this.federatedIdentities.stream().filter(i -> Objects.equals(i.identityProvider, identityProvider)).findFirst().get();
            return selectedIdentity.getUserId();
        } catch (Exception e) {
            log.warning(identityProvider + " is not a valid identity provider for " + this.username)  ;
            return null;
        }
    }

    public KeycloakUserWithoutPicture(KeycloakUser user) {
        this.id = user.getId();
        this.createdTimestamp = user.getCreatedTimestamp();
        this.username = user.getUsername();
        this.enabled = user.isEnabled();
        this.totp = user.isTotp();
        this.emailVerified = user.isEmailVerified();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.email = user.getEmail();
        this.attributes = user.getAttributes();
        this.federatedIdentities = user.getFederatedIdentities();
    }
}