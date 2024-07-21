package com.oglimmer.kniffel.web;

import com.oglimmer.kniffel.model.BookingType;
import com.oglimmer.kniffel.model.KniffelGame;
import com.oglimmer.kniffel.service.GameService;
import com.oglimmer.kniffel.web.dto.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.constraints.NotNull;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

@RestController
@RequestMapping("/api/v1/game")
@CrossOrigin
public class GameController {

    @Autowired
    private GameService gameService;

    @Autowired
    private ModelMapper modelMapper;

    @Operation(
            summary = "Create a new game with a specific number of players",
            description = "The number of players must be at least 2. All player names must be different.", responses = {
            @ApiResponse(
                    responseCode = "201", description = "Created", content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = GameResponse.class)))})
    @PostMapping("/")
    public @NotNull GameResponse createGame(
            // spring needs to know where this Java method parameter is coming from
            // @RequestBody means it's the http request body
            @RequestBody CreateGameRequest createGameRequest) {
        KniffelGame game = gameService.createGame(createGameRequest.getPlayerNames());
        return mapGameResponse(game);
    }

    @Operation(summary = "Provides all information for a game. Can be called at any point in time.", responses = {
            @ApiResponse(
                    responseCode = "200", description = "Game found and data returned", content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = GameResponse.class)))})
    // see that gameId is inside {} to make it a path variable
    @GetMapping("/{gameId}")
    public @NotNull GameResponse getGameInfo(
            // the annotation @PathVariable tells spring to take the parameter data from the URL
            @PathVariable String gameId) {
        KniffelGame game = gameService.getGameInfo(gameId);
        return mapGameResponse(game);
    }

    @Operation(summary = "Re-roll the dice. Can only be called in gameState = ROLL", responses = {
            @ApiResponse(
                    responseCode = "200", description = "Dice rolled", content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = GameResponse.class)))})
    @PostMapping("/{gameId}/roll")
    public @NotNull GameResponse roll(@PathVariable String gameId, @RequestBody DiceRollRequest diceRollRequest) {
        KniffelGame kniffelGame = gameService.getGameInfo(gameId);
        gameService.roll(kniffelGame, diceRollRequest.getDiceToKeep());
        return mapGameResponse(kniffelGame);
    }

    @Operation(summary = "Book the current dice roll under a specific category. Can only be called in gameState = BOOK", responses = {
            @ApiResponse(
                    responseCode = "200", description = "Dice roll booked to category", content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = GameResponse.class)))})
    @PostMapping("/{gameId}/book")
    public @NotNull GameResponse book(@PathVariable String gameId, @RequestBody BookRollRequest bookRollRequest) {
        KniffelGame kniffelGame = gameService.getGameInfo(gameId);
        BookingType enumBookingType = BookingType.valueOf(bookRollRequest.getBookingType());
        gameService.bookRoll(kniffelGame, enumBookingType);
        return mapGameResponse(kniffelGame);
    }

    private GameResponse mapGameResponse(KniffelGame game) {
        // to copy all attributes with the same name we use modelMapper method
        GameResponse gameResponse = modelMapper.map(game, GameResponse.class);
        // we have to copy the PlayerData for each object
        gameResponse.setPlayerData(game.getPlayers().values().stream().map(p -> modelMapper.map(p, PlayerData.class)).toArray(PlayerData[]::new));
        // same for the already used booking types
        gameResponse.setUsedBookingTypes(game.getCurrentPlayer().getUsedBookingTypes().stream().map(Enum::toString).toArray(String[]::new));
        // and the not used bookings types
        gameResponse.setAvailableBookingTypes(Arrays.stream(BookingType.values()).filter(bt -> !game.getCurrentPlayer().getUsedBookingTypes().contains(bt)).map(Enum::toString).toArray(String[]::new));
        return gameResponse;
    }
}