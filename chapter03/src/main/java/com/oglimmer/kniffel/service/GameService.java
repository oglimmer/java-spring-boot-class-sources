package com.oglimmer.kniffel.service;

import com.oglimmer.kniffel.model.BookingType;
import com.oglimmer.kniffel.model.KniffelGame;
import com.oglimmer.kniffel.model.KniffelPlayer;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class GameService {


    // This attribute acts as our "database backend"!
    // we keep track of all created games and
    // make those accessible by their game ID.
    // - key = game ID
    // - value = the actual game object
    private final Map<String, KniffelGame> games = new HashMap<>();

    public KniffelGame createGame(String[] playerNames) {
        // The constructor of KniffelGame needs a java.util.List of
        // KniffelPlayer. KniffelPlayer is the game logic object for a Player.
        List<KniffelPlayer> players =
                Arrays.stream(playerNames) // String[] to stream
                        .map(name -> new KniffelPlayer(name)) // create 1 object for each name
                        .collect(Collectors.toList()); // convert the stream to List

        // now we can create the game logic object
        KniffelGame kniffelGame = new KniffelGame(players);

        // we store in memory, so we can use it in later REST calls
        games.put(kniffelGame.getGameId(), kniffelGame);
        return kniffelGame;
    }

    public KniffelGame getGameInfo(String gameId) {
        return games.get(gameId);
    }

    public void roll(KniffelGame kniffelGame, int[] diceToKeep) {
        kniffelGame.reRollDice(diceToKeep);
    }

    public void bookRoll(KniffelGame kniffelGame, BookingType enumBookingType) {
        kniffelGame.bookDiceRoll(enumBookingType);
    }

}