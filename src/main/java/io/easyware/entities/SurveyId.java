package io.easyware.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.java.Log;
import org.jboss.resteasy.annotations.jaxrs.PathParam;

import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Log
@ToString
public class SurveyId implements Serializable {

    private static final long serialVersionUID = 9050271702821819647L;
    @PathParam
    protected String userId;
    @PathParam
    protected long questionId;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SurveyId that = (SurveyId) o;
        return Objects.equals(userId, that.userId) && Objects.equals(questionId, that.questionId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, questionId);
    }
}
