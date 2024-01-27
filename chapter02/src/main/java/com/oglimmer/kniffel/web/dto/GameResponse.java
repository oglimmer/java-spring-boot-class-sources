package com.oglimmer.kniffel.web.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class GameResponse {
    private String gameId; // secret ID to access the game
    private PlayerData[] playerData; // returns the score per player
    private String currentPlayerName;

    // either BOOK or ROLL
    // for BOOK the /book endpoint needs to be called
    // for ROLL the /roll endpoint needs to be called
    private String state;
    private String[] usedBookingTypes; // returns the booking types used by the current player

    // returns the available booking types for the current player
    // this string should be used for the /book endpoint
    private String[] availableBookingTypes;
    private int[] diceRolls; // dice values the player rolled (1...6). array size = 5
    private int rollRound; // for state==ROLL, returns the round 1,2,3
}