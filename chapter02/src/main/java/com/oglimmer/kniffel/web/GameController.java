package com.oglimmer.kniffel.web;

import com.oglimmer.kniffel.web.dto.BookRollRequest;
import com.oglimmer.kniffel.web.dto.CreateGameRequest;
import com.oglimmer.kniffel.web.dto.DiceRollRequest;
import com.oglimmer.kniffel.web.dto.GameResponse;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/game")
@CrossOrigin
public class GameController {

    @PostMapping("/")
    public GameResponse createGame(
            // spring needs to know where this Java method parameter is coming from
            // @RequestBody means it's the http request body
            @RequestBody CreateGameRequest createGameRequest) {
        return new GameResponse();
    }

    // see that gameId is inside {} to make it a path variable
    @GetMapping("/{gameId}")
    public GameResponse getGameInfo(
            // the annotation @PathVariable tells spring to take the parameter data from the URL
            @PathVariable String gameId) {
        return new GameResponse();
    }

    @PostMapping("/{gameId}/roll")
    public GameResponse roll(@PathVariable String gameId, @RequestBody DiceRollRequest diceRollRequest) {
        return new GameResponse();
    }

    @PostMapping("/{gameId}/book")
    public GameResponse book(@PathVariable String gameId, @RequestBody BookRollRequest bookRollRequest) {
        return new GameResponse();
    }
}