package com.oglimmer.kniffel.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Schema(description = "Game creation request")
@Getter
@Setter
// this creates a method "toString()" which returns all attributes of the class
// we need this to make System.out.println work on this class
@ToString
public class CreateGameRequest {
    @Schema(description = "Defines the participating players. Each player name must be different and at least 2 players have to be provided",
            example = "[\"john doe\", \"jane doe\"]")
    private String[] playerNames;
}
