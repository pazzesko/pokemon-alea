package com.example.pokemonalea.application.pokeapi;

import com.example.pokemonalea.domain.dto.PokemonDTO;
import com.example.pokemonalea.domain.response.PokeApiKeysResponse;
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
public class PokeApiClient {

    private static final String BASE_URL = "https://pokeapi.co/api/v2";

    Logger logger = LoggerFactory.getLogger(PokeApiClient.class);

    private final RestTemplate restTemplate;

    @Autowired
    public PokeApiClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<String> getPokemonNamesByGeneration(int generationId) {
        PokeApiKeysResponse response = null;
        String url = BASE_URL + "/generation/" + generationId;
        try {
            response = restTemplate.getForObject(url, PokeApiKeysResponse.class);
            logger.info("Request: " + url);
        } catch (RestClientException e) {
            logger.error("Error in Request: " + url + " with message: " + e.getMessage());
        }

        return response != null
                ? response.getPokemonSpecies().stream()
                .map(PokeApiKeysResponse.PokemonSpecies::getName)
                .collect(Collectors.toList())
                : Collections.emptyList();
    }

    public List<PokemonDTO> getPokemonsByVersion(List<String> pokemonNames, String version) {
        List<PokemonDTO> pokemonDTOS = new ArrayList<>();
        for (String pokemonName: pokemonNames) {
            String url = BASE_URL + "/pokemon/" + pokemonName;

            try {
                PokemonDTO pokemon = restTemplate.getForObject(url, PokemonDTO.class);

                if (isVersion(pokemon, version)) {
                    pokemonDTOS.add(pokemon);
                }

                logger.info("Request: " + url);
            } catch (RestClientException e) {
                logger.error("Error in Request: " + url + " with message: " + e.getMessage());
            }
        }

        return pokemonDTOS;
    }

    private boolean isVersion(PokemonDTO pokemon, String version) {
        boolean isVersion = false;

        if (pokemon != null) {
            Optional<PokemonDTO.GameIndex> pokemonVersion = pokemon.getGameIndices()
                    .stream()
                    .filter(gameIndex -> version.equals(gameIndex.getVersion().getName()))
                    .findFirst();

            isVersion = pokemonVersion.isPresent();
        }

        return isVersion;
    }
}
