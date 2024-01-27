package com.oglimmer.kniffel.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Schema(description = "Re-roll dice request")
@Getter
@Setter
@ToString
public class DiceRollRequest {
    @Schema(description = "Dice to keep. Must be a subset of the current roll.",
            example = "[1, 2, 3, 4, 5]")
    // dice values ranging from 1 to 6
    // must not exceed 5 elements
    // only dice values returned by diceRolls are allowed
    private int[] diceToKeep;
}
