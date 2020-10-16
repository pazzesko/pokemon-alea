package com.example.pokemonalea.application.cache;

import com.example.pokemonalea.application.pokeapi.PokeApiClient;
import com.example.pokemonalea.application.service.PokemonController;
import com.example.pokemonalea.domain.dto.PokemonDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PokemonCache {

    private static final int GENERATION_RED_BLUE = 1;
    private static final String RED_VERSION = "red";

    Logger logger = LoggerFactory.getLogger(PokemonCache.class);

    private final PokeApiClient pokeApiClient;
    private final PokemonController pokemonController;

    @Autowired
    public PokemonCache(PokemonController pokemonController, PokeApiClient pokeApiClient) {
        this.pokemonController = pokemonController;
        this.pokeApiClient = pokeApiClient;
    }

    public void start() {
        long pokeApiStart = System.currentTimeMillis();
        List<String> pokemonNames = pokeApiClient.getPokemonNamesByGeneration(GENERATION_RED_BLUE);
        List<PokemonDTO> pokemonDTOs = pokeApiClient.getPokemonsByVersion(pokemonNames, RED_VERSION);
        long pokeApiFinish = System.currentTimeMillis();

        logger.info("PokeApi pokemons obtained in: " + (pokeApiFinish - pokeApiStart) + " ms");
        pokeApiClient.shutdown();
        insertPokemons(pokemonDTOs);
        logger.info("cache filled with pokemons");
    }

    private void insertPokemons(List<PokemonDTO> pokemons) {
        for (PokemonDTO pokemon: pokemons) {
            pokemonController.create(pokemon);
        }
    }
}
