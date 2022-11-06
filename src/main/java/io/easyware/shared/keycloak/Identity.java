package io.easyware.shared.keycloak;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.java.Log;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Log
public class Identity {
    public String identityProvider;
    public String userId;
    public String userName;
}
