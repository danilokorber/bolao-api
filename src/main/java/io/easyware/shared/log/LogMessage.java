package io.easyware.shared.log;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotBlank;
import java.util.Date;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "logs")
@JsonPropertyOrder({"id", "loggedAt", "type", "message", "userId"})
public class LogMessage {

    @Id
    @NotBlank
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "logged_at", insertable = false, updatable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    @Temporal(TemporalType.TIMESTAMP)
    private Date loggedAt;

    @NotBlank
    private String type;

    @NotBlank
    private String message;

    @Column(name = "user_id")
    private String user;
}
