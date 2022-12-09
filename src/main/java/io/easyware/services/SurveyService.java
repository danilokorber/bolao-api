package io.easyware.services;

import io.easyware.entities.Bet;
import io.easyware.entities.Survey;
import io.easyware.entities.SurveyId;
import io.easyware.entities.Team;
import io.easyware.repositories.SurveyRepository;
import io.easyware.shared.keycloak.KeycloakUser;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.time.OffsetDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class SurveyService {

    @Inject
    EntityManager entityManager;

    private final SurveyRepository repository;

    public SurveyService(SurveyRepository repository) {
        this.repository = repository;
    }

    public Optional<Survey> getSingle(String userId, long questionId) {
        SurveyId id = new SurveyId(userId, questionId);
        return Optional.ofNullable(entityManager.find(Survey.class, id));
    }
    public List<Survey> getAnswersFromUser(KeycloakUser user) {
        return this.repository.find("userId", user.getId()).list();
    }

    public void saveAnswers(List<Survey> answers) {
        answers.forEach(this::saveAnswer);
    }

    @Transactional
    public void saveAnswer(Survey answer) {
        SurveyId id = new SurveyId(answer.getUserId(), answer.getQuestionId());
        Optional<Survey> existingAnswer = Optional.ofNullable(entityManager.find(Survey.class, id));
        if (existingAnswer.isPresent()) {
            existingAnswer.get().setAnswer(answer.getAnswer());
            existingAnswer.get().setComments(answer.getComments());
            existingAnswer.get().setSavedAt(OffsetDateTime.now());
            existingAnswer.get().persistAndFlush();
        } else {
            answer.setSavedAt(OffsetDateTime.now());
            answer.persistAndFlush();
        }
    }
}
