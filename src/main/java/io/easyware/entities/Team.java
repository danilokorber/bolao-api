package io.easyware.entities;

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
@Table(name = "team")
public class Team {

    @Id
    @NotBlank
    private int id;

    @NotBlank
    @Column(name = "short_name")
    private String shortName;

    @NotBlank
    private String en;

    @NotBlank
    private String de;

    @NotBlank
    private String pt;

}