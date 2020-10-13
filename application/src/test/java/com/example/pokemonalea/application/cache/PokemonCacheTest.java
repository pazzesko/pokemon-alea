package com.example.pokemonalea.application.cache;

import com.example.pokemonalea.application.pokeapi.PokeApiClient;
import com.example.pokemonalea.application.service.PokemonController;
import com.example.pokemonalea.domain.dto.PokemonDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PokemonCacheTest {

    @Mock
    private PokeApiClient pokeApiClient;
    @Mock
    private PokemonController pokemonController;

    private PokemonCache pokemonCache;

    @BeforeEach
    void initPokemonCache() {
        pokemonCache = new PokemonCache(pokemonController, pokeApiClient);
    }

    @Test
    void whenGetPokemonNamesByGenerationIsEmptyThenNoInserts() {
        when(pokeApiClient.getPokemonNamesByGeneration(1)).thenReturn(Collections.emptyList());

        pokemonCache.start();

        verify(pokemonController, never()).create(any(PokemonDTO.class));
    }

    @Test
    void whenGetPokemonsByVersionIsEmptyThenNoInserts() {
        List<String> generationResponse = Collections.singletonList("ALEA");
        when(pokeApiClient.getPokemonNamesByGeneration(1)).thenReturn(generationResponse);
        when(pokeApiClient.getPokemonsByVersion(generationResponse, "red")).thenReturn(Collections.emptyList());

        pokemonCache.start();

        verify(pokemonController, never()).create(any(PokemonDTO.class));
    }

    @Test
    void whenPokemonRedReturnsPokemonsThenPokemonsInserted() {
        List<String> generationResponse = Collections.singletonList("PIKACHU");
        when(pokeApiClient.getPokemonNamesByGeneration(1)).thenReturn(generationResponse);
        PokemonDTO expectedPokemon = createPokemonDTO("PIKACHU", "red");

        when(pokeApiClient.getPokemonsByVersion(generationResponse, "red")).thenReturn(Collections.singletonList(expectedPokemon));

        pokemonCache.start();

        verify(pokemonController, times(1)).create(expectedPokemon);
    }

    private PokemonDTO createPokemonDTO(String pokemonName, String versionName) {
        PokemonDTO pokemonDTO = new PokemonDTO();
        pokemonDTO.setId(1L);
        pokemonDTO.setName(pokemonName);
        pokemonDTO.setBaseExperience(1);
        pokemonDTO.setHeight(1);
        pokemonDTO.setWeight(1);

        PokemonDTO.Version version = new PokemonDTO.Version();
        version.setName(versionName);
        PokemonDTO.GameIndex gameIndex = new PokemonDTO.GameIndex();
        gameIndex.setVersion(version);

        pokemonDTO.setGameIndices(Collections.singletonList(gameIndex));

        return pokemonDTO;
    }
}