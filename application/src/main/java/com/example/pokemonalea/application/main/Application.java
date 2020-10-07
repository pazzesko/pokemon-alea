package com.example.pokemonalea.application.main;

import com.example.pokemonalea.application.cache.PokemonCache;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;

@SpringBootApplication(scanBasePackages = "${app.package}")
@EntityScan(basePackages = "${app.package}")
@EnableJpaRepositories(basePackages = "${app.package}")
public class Application {
	Logger logger = LoggerFactory.getLogger(Application.class);
	@Autowired
    PokemonCache pokemonCache;

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}

	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}

	@PostConstruct
	public void initializeMemory() {
		logger.info("Loading cache memory with poke api requests...");
		pokemonCache.start();
		logger.info("The cache memory has been loaded");
	}
}