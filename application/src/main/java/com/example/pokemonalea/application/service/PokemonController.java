package com.example.pokemonalea.application.service;

import com.example.pokemonalea.domain.dto.PokemonDTO;
import com.example.pokemonalea.domain.model.PokemonModel;
import com.example.pokemonalea.domain.service.PokemonService;
import com.example.pokemonalea.persistence.repository.PokemonRepository;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;
import java.util.Random;

@RestController
public class PokemonController implements PokemonService {

    private final PokemonRepository pokemonRepository;
    private final ModelMapper modelMapper;
    private final Random random = new Random();

    public PokemonController(PokemonRepository pokemonRepository, ModelMapper modelMapper) {
        this.pokemonRepository = pokemonRepository;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/pokemons/topWeight")
    @Override
    public List<PokemonModel>  getTopWeight() {
        return pokemonRepository.findTop5ByOrderByWeightDesc();
    }

    @GetMapping("/pokemons/topHeight")
    @Override
    public List<PokemonModel>  getTopHeight() {
        return pokemonRepository.findTop5ByOrderByHeightDesc();
    }

    @GetMapping("/pokemons/topBaseExperience")
    @Override
    public List<PokemonModel>  getTopBaseExperience() {
        return pokemonRepository.findTop5ByOrderByBaseExperienceDesc();
    }

    @GetMapping("/pokemon/{id}")
    @Override
    public PokemonModel getPokemonById(@PathVariable Long id) {
        return pokemonRepository
                .findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "The Pokemon with id=" + id + " doesn't exist."));
    }

    @GetMapping("/pokemons")
    @Override
    public List<PokemonModel> getPokemons() {
        return pokemonRepository.findAll();
    }

    @GetMapping("/pokemon/random")
    @Override
    public PokemonModel getRandom() {
        int totalPokemons = (int) pokemonRepository.count();
        if (totalPokemons == 0) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "There is no pokemons.");
        } else {
            return pokemonRepository.findAll().get(random.nextInt(totalPokemons));
        }
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/pokemon/create")
    @Override
    public PokemonModel create(@RequestBody @Valid PokemonDTO pokemon) {
        PokemonModel pokemonModel = modelMapper.map(pokemon, PokemonModel.class);
        return pokemonRepository.save(pokemonModel);
    }
}
