package com.example.pokemonalea.service;

import com.example.pokemonalea.persistence.PokemonDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class PokemonCache {

    private static final String BASE_URL = "https://pokeapi.co/api/v2";
    private static final String GENERATION_RED_BLUE = "/generation/1";
    private static final String RED_VERSION = "red";
    private static final String POKEMON = "/pokemon";

    Logger logger = LoggerFactory.getLogger(PokemonCache.class);

    private final RestTemplate restTemplate = new RestTemplate();

    @Autowired
    private PokemonController pokemonController;

    public void start() {
        List<String> pokemonNames = getRedBluePokemons();
        List<PokemonDTO> pokemonDTOs = getRedPokemons(pokemonNames);
        insertPokemons(pokemonDTOs);
    }

    private List<String> getRedBluePokemons() {
        PokeApiKeysResponse response = null;
        String url = BASE_URL + GENERATION_RED_BLUE;
        try {
            response = restTemplate.getForObject(url, PokeApiKeysResponse.class);
            logger.info("Request: " + url);
        } catch (RestClientException e) {
            logger.error(e.getMessage());
        }

        return response != null
                ? response.getPokemonSpecies().stream()
                                            .map(PokeApiKeysResponse.PokemonSpecies::getName)
                                            .collect(Collectors.toList())
                : Collections.emptyList();
    }

    private List<PokemonDTO> getRedPokemons(List<String> pokemonNames) {
        List<PokemonDTO> pokemonDTOS = new ArrayList<>();

        for (String pokemonName: pokemonNames) {
            try {
                String url = BASE_URL + POKEMON + "/" + pokemonName;
                PokemonDTO pokemon = restTemplate.getForObject(url, PokemonDTO.class);

                if (isVersion(pokemon, RED_VERSION)) {
                    pokemonDTOS.add(pokemon);
                }

                logger.info("Request: " + url);
            } catch (RestClientException e) {
                logger.error(e.getMessage());
            }
        }

        return pokemonDTOS;
    }

    private boolean isVersion(PokemonDTO pokemon, String version) {
        boolean isVersion = false;

        if (pokemon != null) {
            Optional<PokemonDTO.GameIndex> redVersion = pokemon.getGameIndices()
                    .stream()
                    .filter(gameIndex -> version.equals(gameIndex.getVersion().getName()))
                    .findFirst();

            isVersion = redVersion.isPresent();
        }

        return isVersion;
    }

    private void insertPokemons(List<PokemonDTO> pokemons) {
        for (PokemonDTO pokemon: pokemons) {
            pokemonController.create(pokemon);
        }
    }
}
