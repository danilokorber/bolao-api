package io.easyware.entities;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.java.Log;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

import static io.easyware.shared.Slug.BASE_URL;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Log
@Table(name = "venue")
@JsonPropertyOrder({"id", "name", "city", "country", "capacity", "googleMapsUrl", "url"})
public class Venue {

    @Id
    @NotBlank
    private long id;

    private String name;
    private String city;
    private String country;
    private int capacity;

    @Column(name = "google_maps")
    private String googleMapsUrl;

    public String getUrl() {
        return BASE_URL + "/venues/" + id;
    }
}
