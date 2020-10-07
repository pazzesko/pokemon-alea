package com.example.pokemonalea.application.service;

import com.example.pokemonalea.domain.dto.PokemonDTO;
import com.example.pokemonalea.domain.model.PokemonModel;
import com.example.pokemonalea.persistence.repository.PokemonRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.Validator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@ExtendWith(MockitoExtension.class)
class PokemonControllerTest {

    private MockMvc mvc;

    @Mock
    private PokemonRepository pokemonRepository;
    @Mock
    private ModelMapper modelMapper;

    @Mock
    private Validator validator;

    private PokemonController pokemonController;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void initPokemonController() {
        pokemonController = new PokemonController(pokemonRepository, modelMapper);
        JacksonTester.initFields(this, new ObjectMapper());
        mvc = MockMvcBuilders.standaloneSetup(pokemonController)
                .setValidator(validator)
                .build();
    }

    @Test
    public void getPokemonByIdWhenExists() throws Exception {
        PokemonModel pokemonModel = createPokemonModel(1L, "ALEA", 1, 1, 1);

        // given
        given(pokemonRepository.findById(1L))
                .willReturn(Optional.of(pokemonModel));

        // when
        // http call
        MockHttpServletResponse response = mvc.perform(
                get("/pokemon/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse();
        // controller call
        PokemonModel controllerModel = pokemonController.getPokemonById(1L);

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(
                objectMapper.writeValueAsString(controllerModel)
        );
        assertThat(response.getContentAsString()).isEqualTo(
                objectMapper.writeValueAsString(pokemonModel)
        );
    }

    @Test
    public void getPokemonByIdThrowsException() throws Exception {
        PokemonModel pokemonModel = createPokemonModel(1L, "ALEA", 1, 1, 1);

        // given
        given(pokemonRepository.findById(1L))
                .willReturn(Optional.empty());

        // when
        // http call
        MockHttpServletResponse response = mvc.perform(
                get("/pokemon/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    @Test
    public void getTopWeightPokemon() throws Exception {
        List<PokemonModel> top5Weight = createTopOrderByWeight();

        // given
        given(pokemonRepository.findTop5ByOrderByWeightDesc())
                .willReturn(top5Weight);

        // when
        // http call
        MockHttpServletResponse response = mvc.perform(
                get("/pokemon/topWeight")
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse();
        // controller call
        List<PokemonModel> controllerTop = pokemonController.getTopWeight();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(
                objectMapper.writeValueAsString(controllerTop)
        );
        assertThat(response.getContentAsString()).isEqualTo(
                objectMapper.writeValueAsString(top5Weight)
        );
    }

    @Test
    public void getTopHeightPokemon() throws Exception {
        List<PokemonModel> top5Height = createTopOrderByHeight();

        // given
        given(pokemonRepository.findTop5ByOrderByHeightDesc())
                .willReturn(top5Height);

        // when
        // http call
        MockHttpServletResponse response = mvc.perform(
                get("/pokemon/topHeight")
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse();
        // controller call
        List<PokemonModel> controllerTop = pokemonController.getTopHeight();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(
                objectMapper.writeValueAsString(controllerTop)
        );
        assertThat(response.getContentAsString()).isEqualTo(
                objectMapper.writeValueAsString(top5Height)
        );
    }

    @Test
    public void getTopBaseExperiencePokemon() throws Exception {
        List<PokemonModel> top5BaseExperience = createTopOrderByBaseExperience();

        // given
        given(pokemonRepository.findTop5ByOrderByBaseExperienceDesc())
                .willReturn(top5BaseExperience);

        // when
        // http call
        MockHttpServletResponse response = mvc.perform(
                get("/pokemon/topBaseExperience")
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse();
        // controller call
        List<PokemonModel> controllerTop = pokemonController.getTopBaseExperience();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(
                objectMapper.writeValueAsString(controllerTop)
        );
        assertThat(response.getContentAsString()).isEqualTo(
                objectMapper.writeValueAsString(top5BaseExperience)
        );
    }

    @Test
    public void getAllPokemon() throws Exception {
        List<PokemonModel> allPokemon = Collections.singletonList(createPokemonModel(1L, "ALEA", 1, 1, 1));

        // given
        given(pokemonRepository.findAll())
                .willReturn(allPokemon);

        // when
        // http call
        MockHttpServletResponse response = mvc.perform(
                get("/pokemon/all")
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse();
        // controller call
        List<PokemonModel> controllerAll = pokemonController.getAll();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(
                objectMapper.writeValueAsString(controllerAll)
        );
        assertThat(response.getContentAsString()).isEqualTo(
                objectMapper.writeValueAsString(allPokemon)
        );
    }

    @Test
    public void getRandomPokemon() throws Exception {
        PokemonModel pokemonModel = createPokemonModel(1L, "ALEA", 1, 1, 1);

        // given
        given(pokemonRepository.findAll())
                .willReturn(Collections.singletonList(pokemonModel));
        given(pokemonRepository.count())
                .willReturn(1L);

        // when
        // http call
        MockHttpServletResponse response = mvc.perform(
                get("/pokemon/random")
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse();
        // controller call
        PokemonModel controllerModel = pokemonController.getRandom();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(
                objectMapper.writeValueAsString(controllerModel)
        );
        assertThat(response.getContentAsString()).isEqualTo(
                objectMapper.writeValueAsString(pokemonModel)
        );
    }

    @Test
    public void getRandomPokemonThrowsException() throws Exception {
         given(pokemonRepository.count())
                .willReturn(0L);

        // when
        // http call
        MockHttpServletResponse response = mvc.perform(
                get("/pokemon/random")
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    @Test
    public void createPokemon() throws Exception {
        PokemonModel pokemonModel = createPokemonModel(1L, "ALEA", 1, 1, 1);
        PokemonDTO pokemonDTO = createPokemonDTO(1L, "ALEA", 1, 1, 1);

        when(modelMapper.map(any(PokemonDTO.class), eq(PokemonModel.class))).thenReturn(pokemonModel);

        // given
        given(pokemonRepository.save(pokemonModel))
                .willReturn(pokemonModel);

        // http call
        // when
        MockHttpServletResponse response = mvc.perform(
                post("/pokemon/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(pokemonDTO)))
                .andReturn()
                .getResponse();
        // controller call
        PokemonModel controllerModel = pokemonController.create(pokemonDTO);

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.getContentAsString()).isEqualTo(
                objectMapper.writeValueAsString(controllerModel)
        );
        assertThat(response.getContentAsString()).isEqualTo(
                objectMapper.writeValueAsString(pokemonModel)
        );
    }

    private List<PokemonModel> createTopOrderByWeight() {
        List<PokemonModel> topWeight = new ArrayList<>();

        topWeight.add(createPokemonModel(5L, "fat", 1, 1, 100));
        topWeight.add(createPokemonModel(4L, "slim", 1, 1, 4));
        topWeight.add(createPokemonModel(3L, "slim", 1, 1, 3));
        topWeight.add(createPokemonModel(2L, "slim", 1, 1, 2));
        topWeight.add(createPokemonModel(1L, "slim", 1, 1, 1));

        return topWeight;
    }

    private List<PokemonModel> createTopOrderByHeight() {
        List<PokemonModel> topHeight = new ArrayList<>();

        topHeight.add(createPokemonModel(5L, "tall", 1, 100, 1));
        topHeight.add(createPokemonModel(4L, "short", 1, 10, 1));
        topHeight.add(createPokemonModel(3L, "short", 1, 3, 1));
        topHeight.add(createPokemonModel(2L, "short", 1, 2, 1));
        topHeight.add(createPokemonModel(1L, "short", 1, 1, 1));

        return topHeight;
    }

    private List<PokemonModel> createTopOrderByBaseExperience() {
        List<PokemonModel> topBaseExperience = new ArrayList<>();

        topBaseExperience.add(createPokemonModel(5L, "experienced", 100, 1, 1));
        topBaseExperience.add(createPokemonModel(4L, "normal", 4, 1, 1));
        topBaseExperience.add(createPokemonModel(3L, "normal", 3, 1, 1));
        topBaseExperience.add(createPokemonModel(2L, "normal", 2, 1, 1));
        topBaseExperience.add(createPokemonModel(1L, "normal", 1, 1, 1));

        return topBaseExperience;
    }

    private PokemonModel createPokemonModel(Long id, String name, Integer baseExperience, Integer height, Integer weight) {
        PokemonModel pokemonModel = new PokemonModel();
        pokemonModel.setId(id);
        pokemonModel.setName(name);
        pokemonModel.setBaseExperience(baseExperience);
        pokemonModel.setHeight(height);
        pokemonModel.setWeight(weight);

        return pokemonModel;
    }

    private PokemonDTO createPokemonDTO(Long id, String name, Integer baseExperience, Integer height, Integer weight) {
        PokemonDTO pokemonDTO = new PokemonDTO();
        pokemonDTO.setId(id);
        pokemonDTO.setName(name);
        pokemonDTO.setBaseExperience(baseExperience);
        pokemonDTO.setHeight(height);
        pokemonDTO.setWeight(weight);

        PokemonDTO.Version version = new PokemonDTO.Version();
        version.setName("red");
        PokemonDTO.GameIndex gameIndex = new PokemonDTO.GameIndex();
        gameIndex.setVersion(version);

        pokemonDTO.setGameIndices(Collections.singletonList(gameIndex));

        return pokemonDTO;
    }
}