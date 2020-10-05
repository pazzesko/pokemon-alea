package com.example.pokemonalea.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import javax.annotation.PostConstruct;

@SpringBootApplication(scanBasePackages = "${app.package}")
@EntityScan(basePackages = "${app.package}")
@EnableJpaRepositories(basePackages = "${app.package}")
public class Application {
	@Autowired
	PokemonCache pokemonCache;

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}

	@PostConstruct
	public void initializeMemory() {
		System.out.println("Loading memory with poke api requests...");
		pokemonCache.start();
	}
}