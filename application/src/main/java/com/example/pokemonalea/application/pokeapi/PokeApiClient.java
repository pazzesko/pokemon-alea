package com.example.pokemonalea.application.pokeapi;

import com.example.pokemonalea.domain.dto.PokemonDTO;
import com.example.pokemonalea.domain.response.PokeApiKeysResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.CustomizableThreadFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.*;
import java.util.stream.Collectors;

@Component
public class PokeApiClient {

    private static final String BASE_URL = "https://pokeapi.co/api/v2";

    Logger logger = LoggerFactory.getLogger(PokeApiClient.class);

    private final RestTemplate restTemplate;
    private final ExecutorService executor = Executors.newFixedThreadPool(20, new CustomizableThreadFactory("poke-api-"));

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
            logger.error("Error in Request: " + url, e);
        }

        return response != null
                ? response.getPokemonSpecies().stream()
                .map(PokeApiKeysResponse.PokemonSpecies::getName)
                .collect(Collectors.toList())
                : Collections.emptyList();
    }

    public List<PokemonDTO> getPokemonsByVersion(List<String> pokemonNames, String version) {
        List<Future<PokemonDTO>> futurePokemonDTOS = createGetPokemonTasks(pokemonNames, version);
        return collectGetPokemonTasks(futurePokemonDTOS);
    }

    private List<Future<PokemonDTO>> createGetPokemonTasks(List<String> pokemonNames, String version) {
        List<Future<PokemonDTO>> futurePokemonDTOS = new ArrayList<>();

        for (String pokemonName: pokemonNames) {
            String url = BASE_URL + "/pokemon/" + pokemonName;
            GetPokemonDTOTask task = new GetPokemonDTOTask(restTemplate, url, version);
            futurePokemonDTOS.add(executor.submit(task));
        }

        return futurePokemonDTOS;
    }

    private List<PokemonDTO> collectGetPokemonTasks(List<Future<PokemonDTO>> futurePokemonDTOS) {
        List<PokemonDTO> pokemonDTOS = new ArrayList<>();

        for (Future<PokemonDTO> future: futurePokemonDTOS) {
            try {
                PokemonDTO pokemonDTO = future.get(2, TimeUnit.SECONDS);
                if (pokemonDTO != null) {
                    pokemonDTOS.add(pokemonDTO);
                }
            } catch (InterruptedException | ExecutionException | TimeoutException e) {
                logger.error("Error in the poke-api task: ", e);
            }
        }

        return pokemonDTOS;
    }

    public void shutdown() {
        executor.shutdown();
    }

    class GetPokemonDTOTask implements Callable<PokemonDTO> {

        private final RestTemplate restTemplate;
        private final String url;
        private final String version;

        public GetPokemonDTOTask(RestTemplate restTemplate, String url, String version) {
            this.restTemplate = restTemplate;
            this.url = url;
            this.version = version;
        }

        public PokemonDTO call() {
            try {
                PokemonDTO pokemon = restTemplate.getForObject(url, PokemonDTO.class);

                logger.info("Request: " + url);

                return isVersion(pokemon, version)
                        ? pokemon
                        : null;

            } catch (RestClientException e) {
                logger.error("Error in Request: " + url , e);
                return null;
            }
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
}
