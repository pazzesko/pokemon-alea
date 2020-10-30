package com.example.pokemonalea.domain.pokeapi;

import com.example.pokemonalea.domain.dto.PokemonDTO;

import java.util.List;

public interface PokeApiClient {

    List<String> getPokemonNamesByGeneration(int generationId);

    List<PokemonDTO> getPokemonsByVersion(List<String> pokemonNames, String version);

    void shutdown();
}
