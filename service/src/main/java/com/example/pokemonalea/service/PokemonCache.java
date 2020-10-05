package com.example.pokemonalea.service;

import com.example.pokemonalea.persistence.PokemonDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class PokemonCache {

    private static final String BASE_URL = "https://pokeapi.co/api/v2";
    private static final String GENERATION_RED = "/generation/1";
    private static final String POKEMON = "/pokemon";

    private final RestTemplate restTemplate = new RestTemplate();

    @Autowired
    private PokemonController pokemonController;

    public void start() {
        List<String> pokemonNames = getRedPokemons();
        List<PokemonDTO> pokemonDTOs = getPokemonDTOs(pokemonNames);
        insertPokemons(pokemonDTOs);
    }

    private List<String> getRedPokemons() {
        PokeApiKeysResponse response = null;

        try {
            response = restTemplate.getForObject(
                    BASE_URL + GENERATION_RED,
                    PokeApiKeysResponse.class);
        } catch (RestClientException e) {
            System.out.println(e.getMessage());
        }

        return response != null
                ? response.pokemon_species.stream()
                .map(ps -> ps.name)
                .collect(Collectors.toList())
                : Collections.emptyList();
    }

    private List<PokemonDTO> getPokemonDTOs(List<String> pokemonNames) {
        List<PokemonDTO> pokemonDTOS = new ArrayList<>();

        for (String pokemonName: pokemonNames) {
            try {
                PokemonDTO pokemon = restTemplate.getForObject(
                        BASE_URL + POKEMON + "/" + pokemonName,
                        PokemonDTO.class);
                pokemonDTOS.add(pokemon);
            } catch (RestClientException e) {
                System.out.println(e.getMessage());
            }
        }

        return pokemonDTOS;
    }

    private void insertPokemons(List<PokemonDTO> pokemons) {
        for (PokemonDTO pokemon: pokemons) {
            pokemonController.create(pokemon);
        }
    }
}
