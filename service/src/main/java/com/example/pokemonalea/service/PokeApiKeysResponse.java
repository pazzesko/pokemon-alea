package com.example.pokemonalea.service;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PokeApiKeysResponse {
    public List<PokemonSpecies> pokemon_species;
}
