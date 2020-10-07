package com.example.pokemonalea.application.cache;

import com.example.pokemonalea.application.service.PokemonController;
import com.example.pokemonalea.domain.dto.PokemonDTO;
import com.example.pokemonalea.domain.response.PokeApiKeysResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PokemonCacheTest {

    @Mock
    private RestTemplate restTemplate;
    @Mock
    private PokemonController pokemonController;

    private PokemonCache pokemonCache;

    @BeforeEach
    void initPokemonCache() {
        pokemonCache = new PokemonCache(pokemonController, restTemplate);
    }

    @Test
    void whenGetRedBluePokemonsResponseIsNullThenNoInserts() {
        String url = "https://pokeapi.co/api/v2/generation/1";
        when(restTemplate.getForObject(url, PokeApiKeysResponse.class)).thenReturn(null);

        pokemonCache.start();

        verify(pokemonController, never()).create(any(PokemonDTO.class));
    }

    @Test
    void whenGetRedBluePokemonsThrowsRestClientExceptionThenNoInserts() {
        String url = "https://pokeapi.co/api/v2/generation/1";
        when(restTemplate.getForObject(url, PokeApiKeysResponse.class)).thenThrow(RestClientException.class);

        pokemonCache.start();

        verify(pokemonController, never()).create(any(PokemonDTO.class));
    }

    @Test
    void whenGetRedPokemonsResponseIsNullThenNoInserts() {
        mockGenerationResponse();

        String urlPokemon = "https://pokeapi.co/api/v2/pokemon/ALEA";
        when(restTemplate.getForObject(urlPokemon, PokemonDTO.class)).thenReturn(null);

        pokemonCache.start();

        verify(pokemonController, never()).create(any(PokemonDTO.class));
    }

    @Test
    void whenGetRedPokemonsThrowsRestClientExceptionThenNoInserts() {
        mockGenerationResponse();

        String urlPokemon = "https://pokeapi.co/api/v2/pokemon/ALEA";
        when(restTemplate.getForObject(urlPokemon, PokemonDTO.class)).thenThrow(RestClientException.class);

        pokemonCache.start();

        verify(pokemonController, never()).create(any(PokemonDTO.class));
    }

    @Test
    void whenPokemonNotRedThenPokemonNotInserted() {
        mockGenerationResponse();
        mockPokemonResponse("blue");

        pokemonCache.start();

        verify(pokemonController, never()).create(any(PokemonDTO.class));
    }

    @Test
    void whenPokemonRedThenPokemonInserted() {
        mockGenerationResponse();
        PokemonDTO expectedPokemon = mockPokemonResponse("red");

        pokemonCache.start();

        verify(pokemonController, times(1)).create(expectedPokemon);
    }

    private void mockGenerationResponse() {
        PokeApiKeysResponse response = new PokeApiKeysResponse();
        PokeApiKeysResponse.PokemonSpecies pokemonSpecies = new PokeApiKeysResponse.PokemonSpecies();
        pokemonSpecies.setName("ALEA");
        response.setPokemonSpecies(Collections.singletonList(pokemonSpecies));

        String urlGeneration = "https://pokeapi.co/api/v2/generation/1";
        when(restTemplate.getForObject(urlGeneration, PokeApiKeysResponse.class)).thenReturn(response);
    }

    private PokemonDTO mockPokemonResponse(String versionName) {
        PokemonDTO pokemonDTO = new PokemonDTO();
        pokemonDTO.setId(1L);
        pokemonDTO.setName("ALEA");
        pokemonDTO.setBaseExperience(1);
        pokemonDTO.setHeight(1);
        pokemonDTO.setWeight(1);

        PokemonDTO.Version version = new PokemonDTO.Version();
        version.setName(versionName);
        PokemonDTO.GameIndex gameIndex = new PokemonDTO.GameIndex();
        gameIndex.setVersion(version);

        pokemonDTO.setGameIndices(Collections.singletonList(gameIndex));

        String urlPokemon = "https://pokeapi.co/api/v2/pokemon/ALEA";
        when(restTemplate.getForObject(urlPokemon, PokemonDTO.class)).thenReturn(pokemonDTO);

        return pokemonDTO;
    }
}