package com.oglimmer.kniffel.web;

import com.oglimmer.kniffel.db.entity.BookingType;
import com.oglimmer.kniffel.db.entity.KniffelGame;
import com.oglimmer.kniffel.service.GameNotFoundException;
import com.oglimmer.kniffel.service.GameService;
import com.oglimmer.kniffel.service.IllegalGameStateException;
import com.oglimmer.kniffel.web.dto.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;

@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/game")
@CrossOrigin
public class GameController {

    private final GameService gameService;
    private final ModelMapper modelMapper;

    @Operation(
            summary = "Create a new game with a specific number of players",
            description = "The number of players must be at least 2. All player names must be different.", responses = {
            @ApiResponse(
                    responseCode = "201", description = "Created", content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = GameResponse.class)))})
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/")
    public GameResponse createGame(
            // spring needs to know where this Java method parameter is coming from
            // @RequestBody means it's the http request body
            @RequestBody CreateGameRequest createGameRequest) {
        if (createGameRequest == null || createGameRequest.getPlayerNames() == null || createGameRequest.getPlayerNames().length < 2) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "At least 2 players have to be provided");
        }
        if (Arrays.stream(createGameRequest.getPlayerNames()).distinct().count() != createGameRequest.getPlayerNames().length) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "All player names must be different");
        }
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
    public GameResponse getGameInfo(
            // the annotation @PathVariable tells spring to take the parameter data from the URL
            @PathVariable @NonNull @lombok.NonNull String gameId) {
        KniffelGame game = gameService.getGameInfo(gameId);
        return mapGameResponse(game);
    }

    @Operation(summary = "Re-roll the dice. Can only be called in gameState = ROLL", responses = {
            @ApiResponse(
                    responseCode = "200", description = "Dice rolled", content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = GameResponse.class)))})
    @PostMapping("/{gameId}/roll")
    public GameResponse roll(@PathVariable @NonNull @lombok.NonNull String gameId, @RequestBody DiceRollRequest diceRollRequest) {
        KniffelGame kniffelGame = gameService.roll(gameId, diceRollRequest.getDiceToKeep());
        return mapGameResponse(kniffelGame);
    }

    @Operation(summary = "Book the current dice roll under a specific category. Can only be called in gameState = BOOK", responses = {
            @ApiResponse(
                    responseCode = "200", description = "Dice roll booked to category", content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = GameResponse.class)))})
    @PostMapping("/{gameId}/book")
    public GameResponse book(@PathVariable @NonNull @lombok.NonNull String gameId, @RequestBody BookRollRequest bookRollRequest) {
        try {
            BookingType enumBookingType = BookingType.valueOf(bookRollRequest.getBookingType());
            KniffelGame kniffelGame = gameService.bookRoll(gameId, enumBookingType);
            return mapGameResponse(kniffelGame);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Undefined bookingTyp");
        }
    }

    private GameResponse mapGameResponse(KniffelGame game) {
        // to copy all attributes with the same name we use modelMapper method
        GameResponse gameResponse = modelMapper.map(game, GameResponse.class);
        // we have to copy the PlayerData for each object
        gameResponse.setPlayerData(game.getPlayers().stream().map(p -> modelMapper.map(p, PlayerData.class)).toArray(PlayerData[]::new));
        // same for the already used booking types
        gameResponse.setUsedBookingTypes(game.getCurrentPlayer().getUsedBookingTypes().stream().map(Enum::toString).toArray(String[]::new));
        // and the not used bookings types
        gameResponse.setAvailableBookingTypes(Arrays.stream(BookingType.values()).filter(bt -> !game.getCurrentPlayer().getUsedBookingTypes().contains(bt)).map(Enum::toString).toArray(String[]::new));
        return gameResponse;
    }

    @ExceptionHandler(IllegalGameStateException.class)
    public void handleException(IllegalGameStateException e) {
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    @ExceptionHandler(GameNotFoundException.class)
    public void handleException() {
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Game not found");
    }
}