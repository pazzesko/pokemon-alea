package com.example.pokemonalea.domain.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class PokemonDTO {

    @NotEmpty(message = "The id of the pokemon cannot be empty")
    private Long id;

    @NotEmpty(message = "The name of the pokemon cannot be empty")
    private String name;

    @NotEmpty(message = "The base experience of the pokemon cannot be empty")
    @JsonProperty("base_experience")
    private Integer baseExperience;

    @NotEmpty(message = "The height of the pokemon cannot be empty")
    private Integer height;

    @NotEmpty(message = "The weight of the pokemon cannot be empty")
    private Integer weight;

    @NotEmpty(message = "The version of the pokemon cannot be empty")
    @JsonProperty("game_indices")
    private List<GameIndex> gameIndices;

    @Getter
    @Setter
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class GameIndex {
        private Version version;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Version {
        private String name;
    }
}
