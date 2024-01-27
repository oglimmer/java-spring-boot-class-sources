package com.oglimmer.kniffel.web.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class DiceRollRequest {
    // dice values ranging from 1 to 6
    // must not exceed 5 elements
    // only dice values returned by diceRolls are allowed
    private int[] diceToKeep;
}