package com.example.pokemonalea.persistence.repository;

import com.example.pokemonalea.domain.model.PokemonModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PokemonRepository extends JpaRepository<PokemonModel, Long> {
    List<PokemonModel> findTop5ByOrderByBaseExperienceDesc();

    List<PokemonModel> findTop5ByOrderByWeightDesc();

    List<PokemonModel> findTop5ByOrderByHeightDesc();
}
