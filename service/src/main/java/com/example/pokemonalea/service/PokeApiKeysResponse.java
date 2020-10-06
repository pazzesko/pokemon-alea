package com.example.pokemonalea.service;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PokeApiKeysResponse {
    @JsonProperty("pokemon_species")
    private List<PokemonSpecies> pokemonSpecies;

    public List<PokemonSpecies> getPokemonSpecies() {
        return pokemonSpecies;
    }

    public void setPokemonSpecies(List<PokemonSpecies> pokemonSpecies) {
        this.pokemonSpecies = pokemonSpecies;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class PokemonSpecies {
        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
