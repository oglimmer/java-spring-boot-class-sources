package com.oglimmer.kniffel.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Schema(description = "Player to score model")
@Getter
@Setter
@ToString
public class PlayerData {
    @Schema(description = "Player name. Given at game creation", example = "john doe")
    private String name;
    @Schema(description = "Current total score of the player", example = "30")
    private int score; // total score so far
}
