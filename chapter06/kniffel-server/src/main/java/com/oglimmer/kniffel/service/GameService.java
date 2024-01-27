package com.oglimmer.kniffel.service;

import com.oglimmer.kniffel.db.entity.BookingType;
import com.oglimmer.kniffel.db.entity.KniffelGame;
import com.oglimmer.kniffel.db.entity.KniffelPlayer;
import com.oglimmer.kniffel.db.repository.KniffelGameRepository;
import com.oglimmer.kniffel.db.repository.KniffelPlayerRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
// make all methods transactional, so they are executed in one transaction,
// meaning either all or none of the operations are executed
@Transactional
public class GameService {

    private final KniffelGameRepository kniffelGameRepository;
    private final KniffelPlayerRepository kniffelPlayerRepository;
    private final KniffelRules kniffelRules;

    public KniffelGame createGame(@NonNull @lombok.NonNull String[] playerNames) {
        // using Java's streaming API to convert string[] into KniffelPlayer and create a List
        List<KniffelPlayer> players = Arrays
                .stream(playerNames)
                .map(KniffelPlayer::new)
                .collect(Collectors.toList());
        kniffelPlayerRepository.saveAll(players);
        KniffelGame kniffelGame = new KniffelGame(players);
        kniffelGameRepository.save(kniffelGame);
        return kniffelGame;
    }

    public KniffelGame getGameInfo(@NonNull @lombok.NonNull String gameId) {
        return kniffelGameRepository.findByGameId(gameId).orElseThrow(GameNotFoundException::new);
    }

    /**
     * Re-rolls all, some or no dice.
     *
     * @param diceToKeep the dice value to keep, so it only re-rolls the other dice, may be null or empty
     */
    public KniffelGame roll(@NonNull @lombok.NonNull String gameId, int[] diceToKeep) {
        KniffelGame kniffelGame = getGameInfo(gameId);
        kniffelGame.roll(diceToKeep);
        return kniffelGame;
    }

    /**
     * Books the current dice into a booking type. Each booking type must only be used once.
     * This method uses a Spring registered bean named "kniffelRules" implementing the KniffelRules
     *
     * @param bookingType to use for this dice roll
     */
    public KniffelGame bookRoll(@NonNull @lombok.NonNull String gameId, @NonNull @lombok.NonNull BookingType bookingType) {
        KniffelGame kniffelGame = getGameInfo(gameId);
        kniffelGame.bookRoll(bookingType, kniffelRules);
        return kniffelGame;
    }    

}