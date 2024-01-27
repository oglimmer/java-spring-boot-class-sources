package com.oglimmer.kniffel.db.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import org.springframework.lang.NonNull;

import com.oglimmer.kniffel.service.IllegalGameStateException;
import com.oglimmer.kniffel.service.KniffelRules;

@Entity
@Getter
@Setter
@Slf4j
public class KniffelGame {

    @Id
    @GeneratedValue
    private Long id;

    /**
     * player name to player object mapping
     */
    @OneToMany(mappedBy = "game")
    private List<KniffelPlayer> players;

    /**
     * secret game id, knowing this id allows a client to retrieve/alter data
     */
    private String gameId;

    /**
     * in each turn each player can re-roll the dice twice. round keeps track of re-rolls
     * round = 1 : initial roll (happens automatically)
     * round = 2 : first re-roll
     * round = 3 : second re-roll
     */
    private int rollRound;

    @OneToOne
    private KniffelPlayer currentPlayer;

    @Enumerated(EnumType.STRING)
    private GameState state = GameState.ROLL;

    /**
     * holds the rolled dice values for the current player
     */
    @ElementCollection
    private List<Integer> diceRolls = List.of();

    public KniffelGame() {
    }

    /**
     * Creates a new KniffelGame for a certain number of players and start the game, but doing the first dice
     * roll for the starting player.
     *
     * @param players defines the player names
     */
    public KniffelGame(@NonNull @lombok.NonNull List<KniffelPlayer> players) {
        this.gameId = UUID.randomUUID().toString().replaceAll("-", "");
        this.players = new ArrayList<>();
        this.players.addAll(players);
        this.players.forEach(p -> p.setGame(this));
        currentPlayer = players.getFirst();
        rollRound = 0;
        roll(null);
        log.debug("Game {} created", gameId);
    }

    public void roll(int[] diceToKeep) {
        if (getState() != GameState.ROLL) {
            throw new IllegalGameStateException("Game is not in roll state");
        }
        log.debug("Game {} roll dice. current dice {}, to keep {}", gameId, diceRolls, diceToKeep);
        List<Integer> diceRolls = new ArrayList<>(getDiceRolls());
        removeDice(diceRolls, diceToKeep);
        for (int i = 0; i < 5 - (diceToKeep != null ? diceToKeep.length : 0); i++) {
            diceRolls.add((int) (Math.random() * 6) + 1);
        }
        diceRolls.sort(Comparator.naturalOrder());
        setRollRound(getRollRound() + 1);
        if (getRollRound() == 3) {
            nextPhase();
        }
        setDiceRolls(diceRolls);
        log.debug("Game {} roll dice, done. current dice {}}", gameId, diceRolls);
    }

    public void bookRoll(@NonNull @lombok.NonNull BookingType bookingType, @NonNull @lombok.NonNull KniffelRules kniffelRules) {
        if (getState() != GameState.BOOK) {
            throw new IllegalGameStateException("Game is not in book state");
        }
        if (getCurrentPlayer().getUsedBookingTypes().contains(bookingType)) {
            throw new IllegalStateException("BookingType already used");
        }
        log.debug("Game {} book roll. current dice {}, to booking category {}", gameId, diceRolls, bookingType);
        int toAddScore = bookingType.getToAddScore(getDiceRolls(), kniffelRules);
        getCurrentPlayer().setScore(getCurrentPlayer().getScore() + toAddScore);
        getCurrentPlayer().getUsedBookingTypes().add(bookingType);
        nextPhase();
    }

    private void nextPhase() {
        setState(getState() == GameState.ROLL ? GameState.BOOK : GameState.ROLL);
        log.debug("Game {} next phase. new phase {}", gameId, getState());
        if (getState() == GameState.ROLL) {
            setCurrentPlayer(findNextPlayer());
            setRollRound(0);
            roll(null);
        }
    }

    void removeDice(@NonNull @lombok.NonNull List<Integer> diceRolls, int[] diceToKeep) {
        List<Integer> toKeepList = diceToKeep == null ? new ArrayList<>() : new ArrayList<>(Arrays.stream(diceToKeep).boxed().toList());
        for (Iterator<Integer> iterator = diceRolls.iterator(); iterator.hasNext(); ) {
            Integer diceVal = iterator.next();
            int idx = toKeepList.indexOf(diceVal);
            if (idx == -1) {
                iterator.remove();
            } else {
                toKeepList.remove(idx);
            }
        }
    }

    private KniffelPlayer findNextPlayer() {
        Iterator<KniffelPlayer> iterator = getPlayers().iterator();
        while (iterator.hasNext()) {
            KniffelPlayer player = iterator.next();
            if (player.equals(getCurrentPlayer())) {
                if (iterator.hasNext()) {
                    return iterator.next();
                } else {
                    setRollRound(getRollRound() + 1);
                    return getPlayers().getFirst();
                }
            }
        }
        throw new IllegalStateException("No next player found");
    }

}
