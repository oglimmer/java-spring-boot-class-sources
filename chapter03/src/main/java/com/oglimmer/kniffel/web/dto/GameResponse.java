package com.oglimmer.kniffel.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Game data model. Used for communication between client and server and returned by all endpoints")
@Getter
@Setter
@ToString
public class GameResponse {

    @Schema(description = "Secret game id", example = "1234567890")
    @NotNull
    private String gameId; // secret ID to access the game

    @NotNull
    private PlayerData[] playerData; // returns the score per player

    @Schema(description = "player name whose turn it is", example = "john doe")
    @NotNull
    private String currentPlayerName;

    @Schema(description = "current state of the turn. can either be ROLL or BOOK", example = "BOOK", allowableValues = {"ROLL", "BOOK"})
    @NotNull
    // either BOOK or ROLL
    // for BOOK the /book endpoint needs to be called
    // for ROLL the /roll endpoint needs to be called
    private String state;

    @Schema(description = "all booking types already used by the current player", example = "[\"ONES\", \"TWOS\"]", allowableValues = {"ONES", "TWOS", "THREES", "FOURS", "FIVES", "SIXES", "THREE_OF_A_KIND", "FOUR_OF_A_KIND", "FULL_HOUSE", "SMALL_STRAIGHT", "LARGE_STRAIGHT", "CHANCE", "KNIFFEL"})
    @NotNull
    private String[] usedBookingTypes; // returns the booking types used by the current player

    @Schema(description = "all booking types not used yet by the current player", example = "[\"ONES\", \"TWOS\"]", allowableValues = {"ONES", "TWOS", "THREES", "FOURS", "FIVES", "SIXES", "THREE_OF_A_KIND", "FOUR_OF_A_KIND", "FULL_HOUSE", "SMALL_STRAIGHT", "LARGE_STRAIGHT", "CHANCE", "KNIFFEL"})
    @NotNull
    // returns the available booking types for the current player
    // this string should be used for the /book endpoint
    private String[] availableBookingTypes;

    @Schema(description = "dice values rolled by the current player. Always 5 elements. Values as 6 sided dice, thus from 1 to 6", example = "[1, 2, 2, 3, 6]")
    @NotNull
    private int[] diceRolls; // dice values the player rolled (1...6). array size = 5

    @Schema(description = "Indicated the round during the ROLL state. Can be 1 or 2 as after the 2nd round, the game moved automatically into state BOOK.", example = "1", allowableValues = {"1", "2"})
    @NotNull
    private int rollRound; // for state==ROLL, returns the round 1,2,3
}
