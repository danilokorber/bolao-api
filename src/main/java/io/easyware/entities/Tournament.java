package io.easyware.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.easyware.shared.Slug;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.java.Log;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Log
@Table(name = "tournament")
@JsonPropertyOrder({"id", "url", "name", "editions"})
public class Tournament implements Slug {

    @Id
    @NotBlank
    private long id;

    @NotBlank
    private String name;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name="tournament_id")
    @JsonIgnoreProperties({"groups"})
    private List<TournamentEdition> editions;

    public String getUrl() {
        return "/api/v1/tournaments/" + slugify(name);
    }
}
