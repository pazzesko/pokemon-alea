package com.example.pokemonalea.domain.service;

import com.example.pokemonalea.domain.dto.PokemonDTO;
import com.example.pokemonalea.domain.model.PokemonModel;

import java.util.List;

public interface PokemonService {

    List<PokemonModel> getTopWeight();

    List<PokemonModel>  getTopHeight();

    List<PokemonModel>  getTopBaseExperience();

    PokemonModel getPokemonById(Long id);

    List<PokemonModel> getPokemons();

    PokemonModel getRandom();

    PokemonModel create(PokemonDTO pokemon);
}
