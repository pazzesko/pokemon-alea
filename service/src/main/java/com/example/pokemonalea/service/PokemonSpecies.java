package com.example.pokemonalea.service;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PokemonSpecies {
    public String name;
    public String url;
}
