package io.easyware.entities;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.java.Log;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import java.time.OffsetDateTime;

@Entity
@IdClass(SurveyId.class)
@Table(name = "survey")
@Getter
@Setter
@NoArgsConstructor
@Log
@ToString
public class Survey extends PanacheEntityBase {

    @Id
    @Column(name = "user_id")
    private String userId;

    @Id
    @Column(name = "question_id")
    private long questionId;

    private String answer;

    private String comments;

    @Column(name = "saved_at")
    private OffsetDateTime savedAt;
}
