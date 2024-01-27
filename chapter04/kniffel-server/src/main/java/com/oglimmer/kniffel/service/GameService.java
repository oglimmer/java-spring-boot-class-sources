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
        // using Java's streaming API to convert string[] into KniffelPlayer and create a List
        List<KniffelPlayer> players = Arrays.stream(playerNames).map(KniffelPlayer::new).collect(Collectors.toList());
        KniffelGame kniffelGame = new KniffelGame(players);
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