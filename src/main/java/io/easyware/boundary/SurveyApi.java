package io.easyware.boundary;

import io.easyware.services.SurveyService;
import io.easyware.shared.keycloak.KeycloakService;
import io.easyware.shared.log.Logger;

import javax.inject.Inject;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;

public class SurveyApi {

    @Inject
    SurveyService surveyService;

    @Inject
    KeycloakService keycloakService;

    @Context
    SecurityContext sec;

    @Inject
    Logger log;
}
