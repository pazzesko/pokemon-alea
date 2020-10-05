package com.example.pokemonalea.persistence;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotEmpty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PokemonDTO {

    @NotEmpty(message = "The id of the pokemon cannot be empty")
    private Long id;
    @NotEmpty(message = "The name of the pokemon cannot be empty")
    private String name;
    @NotEmpty(message = "The base experience of the pokemon cannot be empty")
    @JsonProperty("base_experience")
    private Integer baseExperience;
    @NotEmpty(message = "The height of the pokemon cannot be empty")
    private Integer height;
    @NotEmpty(message = "The weight of the pokemon cannot be empty")
    private Integer weight;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getBaseExperience() {
        return baseExperience;
    }

    public void setBaseExperience(Integer baseExperience) {
        this.baseExperience = baseExperience;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }
}
