package com.example.pokemonalea.domain.model;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class PokemonModel {

    @Id
    private Long id;
    private String name;
    private Integer baseExperience;
    private Integer height;
    private Integer weight;
}
