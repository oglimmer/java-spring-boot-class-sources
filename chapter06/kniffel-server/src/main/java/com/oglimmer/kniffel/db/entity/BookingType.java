package com.oglimmer.kniffel.db.entity;

import java.util.List;

import org.springframework.lang.NonNull;

import com.oglimmer.kniffel.service.KniffelRules;

public enum BookingType {
    ONES, TWOS, THREES, FOURS, FIVES, SIXES, THREE_OF_A_KIND, FOUR_OF_A_KIND, FULL_HOUSE, SMALL_STRAIGHT, LARGE_STRAIGHT, KNIFFEL, CHANCE;

    public int getToAddScore(@NonNull @lombok.NonNull List<Integer> diceRolls, @NonNull @lombok.NonNull KniffelRules kniffelRules) {
        return switch (this) {
            case ONES -> kniffelRules.getScore1To6(diceRolls, 1);
            case TWOS -> kniffelRules.getScore1To6(diceRolls, 2);
            case THREES -> kniffelRules.getScore1To6(diceRolls, 3);
            case FOURS -> kniffelRules.getScore1To6(diceRolls, 4);
            case FIVES -> kniffelRules.getScore1To6(diceRolls, 5);
            case SIXES -> kniffelRules.getScore1To6(diceRolls, 6);
            case THREE_OF_A_KIND -> kniffelRules.getScoreForXofAKind(diceRolls, 3);
            case FOUR_OF_A_KIND -> kniffelRules.getScoreForXofAKind(diceRolls, 4);
            case KNIFFEL -> kniffelRules.getScoreKniffel(diceRolls);
            case FULL_HOUSE -> kniffelRules.getScoreFullHouse(diceRolls);
            case SMALL_STRAIGHT -> kniffelRules.getScoreSmallStraight(diceRolls);
            case LARGE_STRAIGHT -> kniffelRules.getScoreLargeStraight(diceRolls);
            case CHANCE -> kniffelRules.getScoreChance(diceRolls);
        };
    }    
}
