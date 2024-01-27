package com.oglimmer.kniffel.logic;


import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import com.oglimmer.kniffel.service.KniffelRules;

public class KniffelRulesImplementation extends KniffelRules {
    public int getScore1To6(List<Integer> diceRolls, int valueToScore) {
        // count all dice values with value == valueToScore
        return diceRolls.stream().filter(d -> d == valueToScore).mapToInt(d -> d).sum();
    }

    public int getScoreForXofAKind(List<Integer> diceRolls, int valueToScore) {
        // check if there are $valueToScore (3 or 4) dice with the same value
        // if yes: count the dice values, else: 0
        return diceRolls.stream()
                .collect(Collectors.groupingBy(d -> d))
                .values()
                .stream()
                .filter(d -> d.size() >= valueToScore)
                .findFirst()
                .map(d -> d.stream().limit(valueToScore).mapToInt(i -> i).sum())
                .orElse(0);
    }

    public int getScoreFullHouse(List<Integer> diceRolls) {
        // check if there are 2 dice with the same value
        // and 3 dice with the same value return 25, else 0;
        if (diceRolls.stream().collect(Collectors.groupingBy(d -> d)).values().stream().filter(d -> d.size() == 3 || d.size() == 2).toList().size() == 2) {
            return 25;
        }
        return 0;
    }

    public int getScoreSmallStraight(List<Integer> diceRolls) {
        // check if 4 out 5 values are [1,2,3,4] or [2,3,4,5] or [3,4,5,6]
        // keep in mind that the order might be different
        // return 30, else 0;
        diceRolls.sort(Comparator.naturalOrder());
        if (diceRolls.containsAll(Arrays.asList(1, 2, 3, 4)) || diceRolls.containsAll(Arrays.asList(2, 3, 4, 5)) || diceRolls.containsAll(Arrays.asList(3, 4, 5, 6))) {
            return 30;
        }
        return 0;
    }

    public int getScoreLargeStraight(List<Integer> diceRolls) {
        // check if [1,2,3,4,5] or [2,3,4,5,6]
        // keep in mind that the order might be different
        // return 40, else 0;
        diceRolls.sort(Comparator.naturalOrder());
        if (diceRolls.equals(Arrays.asList(1, 2, 3, 4, 5)) || diceRolls.equals(Arrays.asList(2, 3, 4, 5, 6))) {
            return 40;
        }
        return 0;
    }

    public int getScoreKniffel(List<Integer> diceRolls) {
        // check if all dice have the same value, return 50, else 0
        if (diceRolls.stream().collect(Collectors.groupingBy(d -> d)).values().stream().anyMatch(d -> d.size() == 5)) {
            return 50;
        }
        return 0;
    }

    public int getScoreChance(List<Integer> diceRolls) {
        // sum up all dice values, no conditions
        return diceRolls.stream().mapToInt(i -> i).sum();
    }
}