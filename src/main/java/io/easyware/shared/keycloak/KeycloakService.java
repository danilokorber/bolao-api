package io.easyware.shared.keycloak;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import io.easyware.shared.http.HttpClient;
import io.quarkus.scheduler.Scheduled;
import lombok.extern.java.Log;
import org.eclipse.microprofile.config.ConfigProvider;

import javax.enterprise.context.ApplicationScoped;
import javax.json.JsonObject;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import static java.util.Map.entry;

@ApplicationScoped
@Log
public class KeycloakService {

    // @Inject Helper helper;

    private Token token;
    private String basicAuth;
    private HttpClient httpClient;
    private List<KeycloakUser> allUsers;

    private final ObjectMapper MAPPER = new ObjectMapper();
    private final String URL_TOKEN = ConfigProvider.getConfig().getValue("keycloak.url.token", String.class);
    private final String URL_FIND_USERS = ConfigProvider.getConfig().getValue("keycloak.url.findUsers", String.class);
    private final String URL_FIND_USER_BY_ID = ConfigProvider.getConfig().getValue("keycloak.url.findUserById", String.class);
    private final String URL_UPDATE_USER = ConfigProvider.getConfig().getValue("keycloak.url.updateUser", String.class);


    private final Map<String, String> tokenRequestBody = Map.ofEntries(
            entry("username", ConfigProvider.getConfig().getValue("keycloak.token.username", String.class)),
            entry("password", ConfigProvider.getConfig().getValue("keycloak.token.password", String.class)),
            entry("grant_type", ConfigProvider.getConfig().getValue("keycloak.token.grant_type", String.class)),
            entry("client_id", ConfigProvider.getConfig().getValue("keycloak.token.client_id", String.class)),
            entry("client_secret", ConfigProvider.getConfig().getValue("keycloak.token.client_secret", String.class))
    );

    public KeycloakService() throws IOException {
        log.info("Building Keycloak. First step: initToken().");
        initToken();
        MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    @Scheduled(every = "120s")
    public void initToken() {
        log.info("Renewing token");
        try {
            httpClient = new HttpClient(URL_TOKEN);
            String res = httpClient.postForm(tokenRequestBody);
            token = MAPPER.readValue(res, Token.class);
            log.info("Token received: " + token.getAccessToken().substring(0, 15) + "(...)");
            basicAuth = "Bearer " + token.getAccessToken();
            updateUserList();
        } catch (Exception e) {
            log.severe(e.getMessage());
        }
    }

    private boolean tokenIsValid() {
        if (!token.isValid()) {
            log.warning("Renewing token...");
            initToken();
        }
        return token.isValid();
    }

    public void updateUserList() throws IOException {
        if (token.isValid()) {
            try {
                httpClient = new HttpClient(URL_FIND_USERS);
                allUsers = MAPPER.readValue(httpClient.get(basicAuth), new TypeReference<List<KeycloakUser>>() {
                });
                httpClient.close();
            } catch (Exception e) {
                initToken();
            }

        }
    }


    public List<KeycloakUser> findAll() {
        return allUsers;
    }
    public KeycloakUser findByMail(String mail) {
        if (tokenIsValid()) {
            try {
                String keycloakId = getKeycloakIdFromMail(mail);
                return findUserByKeycloakId(keycloakId);
            } catch (NoSuchElementException e) {
                log.severe("User not found in Keycloak: " + mail);
            } catch (Exception e) {
                log.severe(e.getClass().getName() + " | " + e.getMessage());
            }
        }
        return null;
    }

    public KeycloakUser findUserByKeycloakId(String keycloakId) throws IOException {
        KeycloakUser user;
        if (tokenIsValid()) {
            try {
                httpClient = new HttpClient(URL_FIND_USER_BY_ID + "/" + keycloakId);
                user = MAPPER.readValue(httpClient.get(basicAuth), KeycloakUser.class);

            } catch (IllegalArgumentException e) {
                initToken();
                httpClient = new HttpClient(URL_FIND_USER_BY_ID + "/" + keycloakId);
                user = MAPPER.readValue(httpClient.get(basicAuth), KeycloakUser.class);
            }
            httpClient.close();
            return user;
        }
        return null;
    }

    public String getKeycloakIdFromMail(String mail) {
        return allUsers.stream().filter(ku -> ku.getEmail().equals(mail)).findFirst().orElseThrow().getId();
    }

    public KeycloakUser persist(KeycloakUserWithoutPicture user) throws IOException {
        if (tokenIsValid()) {
            httpClient = new HttpClient(URL_UPDATE_USER + "/" + user.getId());
            String res = httpClient.put(user, basicAuth);
            updateUserList();
            return findUserByKeycloakId(user.getId());
        }
        return null;
    }
}
