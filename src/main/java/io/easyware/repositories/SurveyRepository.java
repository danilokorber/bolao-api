package io.easyware.repositories;

import io.easyware.entities.Survey;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class SurveyRepository implements PanacheRepository<Survey> {
}
