package com.example.pokemonalea.application.pokeapi;

import com.example.pokemonalea.domain.dto.PokemonDTO;
import com.example.pokemonalea.domain.response.PokeApiKeysResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PokeApiClientImplTest {

    @Mock
    private RestTemplate restTemplate;

    private PokeApiClientImpl pokeApiClientImpl;

    @BeforeEach
    void initPokeApi() {
        pokeApiClientImpl = new PokeApiClientImpl(restTemplate);
    }

    @Test
    void whenGetPokemonNamesByGenerationThrowsExceptionThenReturnEmptyList() {
        when(restTemplate.getForObject("https://pokeapi.co/api/v2/generation/99", PokeApiKeysResponse.class)).thenThrow(RestClientException.class);
        assertThat(pokeApiClientImpl.getPokemonNamesByGeneration(99)).isEmpty();
    }

    @Test
    void whenGetPokemonNamesByGenerationThenReturnNames() {
        PokeApiKeysResponse response = new PokeApiKeysResponse();
        PokeApiKeysResponse.PokemonSpecies pokemonSpecies = new PokeApiKeysResponse.PokemonSpecies();
        pokemonSpecies.setName("ALEA");
        response.setPokemonSpecies(Collections.singletonList(pokemonSpecies));

        when(restTemplate.getForObject("https://pokeapi.co/api/v2/generation/99", PokeApiKeysResponse.class)).thenReturn(response);
        assertThat(pokeApiClientImpl.getPokemonNamesByGeneration(99)).containsExactly("ALEA");
    }

    @Test
    void whenGetPokemonsByVersionThrowsExceptionThenReturnEmptyList() {
        when(restTemplate.getForObject("https://pokeapi.co/api/v2/pokemon/pikachu", PokemonDTO.class)).thenThrow(RestClientException.class);
        assertThat(pokeApiClientImpl.getPokemonsByVersion(Collections.singletonList("pikachu"), "yellow")).isEmpty();
    }

    @Test
    void whenGetPokemonsByVersionThenReturnPokemons() {
        PokemonDTO pokemonYellow = createPokemonDTO("pikachu", "yellow");
        PokemonDTO pokemonRed = createPokemonDTO("charmander", "red");

        when(restTemplate.getForObject("https://pokeapi.co/api/v2/pokemon/pikachu", PokemonDTO.class)).thenReturn(pokemonYellow);
        when(restTemplate.getForObject("https://pokeapi.co/api/v2/pokemon/charmander", PokemonDTO.class)).thenReturn(pokemonRed);

        assertThat(pokeApiClientImpl.getPokemonsByVersion(Arrays.asList("pikachu", "charmander"), "yellow")).containsExactly(pokemonYellow);
    }

    private PokemonDTO createPokemonDTO(String name, String versionName) {
        PokemonDTO pokemonDTO = new PokemonDTO();
        pokemonDTO.setId(1L);
        pokemonDTO.setName(name);
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