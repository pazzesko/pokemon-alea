package com.example.pokemonalea.application.service;

import com.example.pokemonalea.domain.dto.PokemonDTO;
import com.example.pokemonalea.domain.model.PokemonModel;
import com.example.pokemonalea.persistence.repository.PokemonRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;
import java.util.Random;

@RestController
public class PokemonController {

    private final PokemonRepository pokemonRepository;
    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    public PokemonController(PokemonRepository pokemonRepository) {
        this.pokemonRepository = pokemonRepository;
    }

    @GetMapping("/pokemon/topWeight")
    public List<PokemonModel>  getTopWeight() {
        return pokemonRepository.findTop5ByOrderByWeightDesc();
    }

    @GetMapping("/pokemon/topHeight")
    public List<PokemonModel>  getTopHeight() {
        return pokemonRepository.findTop5ByOrderByHeightDesc();
    }

    @GetMapping("/pokemon/topBaseExperience")
    public List<PokemonModel>  getTopBaseExperience() {
        return pokemonRepository.findTop5ByOrderByBaseExperienceDesc();
    }

    @GetMapping("/pokemon/{id}")
    public PokemonModel getPokemonById(@PathVariable Long id) {
        return pokemonRepository
                .findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "The Pokemon with id=" + id + " doesn't exist."));
    }

    @GetMapping("/pokemon/all")
    public List<PokemonModel> all() {
        return pokemonRepository.findAll();
    }

    @GetMapping("/pokemon/random")
    public PokemonModel random() {
        int totalPokemons = (int) pokemonRepository.count();
        if (totalPokemons == 0) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "There is no pokemons.");
        } else {
            return pokemonRepository.findAll().get(new Random().nextInt(totalPokemons));
        }
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/pokemon/create")
    public PokemonModel create(@RequestBody @Valid PokemonDTO pokemon) {
        PokemonModel pokemonModel = modelMapper.map(pokemon, PokemonModel.class);
        return pokemonRepository.save(pokemonModel);
    }

}
