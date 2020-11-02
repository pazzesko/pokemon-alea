package com.example.pokemonalea.application.cache;

import com.example.pokemonalea.domain.dto.PokemonDTO;
import com.example.pokemonalea.domain.model.PokemonModel;
import com.example.pokemonalea.domain.pokeapi.PokeApiClient;
import com.example.pokemonalea.persistence.repository.PokemonRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PokemonCacheTest {

    @Mock
    private PokeApiClient pokeApiClient;
    @Mock
    private PokemonRepository pokemonRepository;

    private final ModelMapper modelMapper = new ModelMapper();

    private PokemonCache pokemonCache;

    @BeforeEach
    void initPokemonCache() {
        pokemonCache = new PokemonCache(pokemonRepository, pokeApiClient, modelMapper);
    }

    @Test
    void whenGetPokemonNamesByGenerationIsEmptyThenNoInserts() {
        when(pokeApiClient.getPokemonNamesByGeneration(1)).thenReturn(Collections.emptyList());

        pokemonCache.start();

        verify(pokemonRepository, never()).save(any(PokemonModel.class));
    }

    @Test
    void whenGetPokemonsByVersionIsEmptyThenNoInserts() {
        List<String> generationResponse = Collections.singletonList("ALEA");
        when(pokeApiClient.getPokemonNamesByGeneration(1)).thenReturn(generationResponse);
        when(pokeApiClient.getPokemonsByVersion(generationResponse, "red")).thenReturn(Collections.emptyList());

        pokemonCache.start();

        verify(pokemonRepository, never()).save(any(PokemonModel.class));
    }

    @Test
    void whenPokemonRedReturnsPokemonsThenPokemonsInserted() {
        List<String> generationResponse = Collections.singletonList("PIKACHU");
        when(pokeApiClient.getPokemonNamesByGeneration(1)).thenReturn(generationResponse);
        PokemonDTO pokemonRed = createPokemonDTO("PIKACHU", "red");
        PokemonModel expectedPokemon = modelMapper.map(pokemonRed, PokemonModel.class);

        when(pokeApiClient.getPokemonsByVersion(generationResponse, "red")).thenReturn(Collections.singletonList(pokemonRed));

        pokemonCache.start();

        verify(pokemonRepository, times(1)).save(refEq(expectedPokemon));
    }

    private PokemonDTO createPokemonDTO(String pokemonName, String versionName) {
        PokemonDTO.Version version = new PokemonDTO.Version();
        version.setName(versionName);
        PokemonDTO.GameIndex gameIndex = new PokemonDTO.GameIndex();
        gameIndex.setVersion(version);

        return PokemonDTO.builder()
                .id(1L)
                .name(pokemonName)
                .baseExperience(1)
                .height(1)
                .weight(1)
                .gameIndices(Collections.singletonList(gameIndex))
                .build();
    }
}