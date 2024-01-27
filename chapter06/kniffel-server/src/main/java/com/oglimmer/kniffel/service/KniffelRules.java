package com.oglimmer.kniffel.service;

import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class KniffelRules {

    public int getScore1To6(List<Integer> diceRolls, int valueToScore) {
        return diceRolls.stream().filter(d -> d == valueToScore).mapToInt(d -> d).sum();
    }    

    public int getScoreForXofAKind(List<Integer> diceRolls, int valueToScore) {
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
        if (diceRolls.stream().collect(Collectors.groupingBy(d -> d)).values().stream().filter(d -> d.size() == 3 || d.size() == 2).toList().size() == 2) {
            return 25;
        }
        return 0;
    }

    public int getScoreSmallStraight(List<Integer> diceRolls) {
        // check if 1,2,3,4 or 2,3,4,5 or 3,4,5,6
        diceRolls.sort(Comparator.naturalOrder());
        if (diceRolls.containsAll(Arrays.asList(1, 2, 3, 4)) || diceRolls.containsAll(Arrays.asList(2, 3, 4, 5)) || diceRolls.containsAll(Arrays.asList(3, 4, 5, 6))) {
            return 30;
        }
        return 0;
    }

    public int getScoreLargeStraight(List<Integer> diceRolls) {
        // check if 1,2,3,4,5 or 2,3,4,5,6
        diceRolls.sort(Comparator.naturalOrder());
        if (diceRolls.equals(Arrays.asList(1, 2, 3, 4, 5)) || diceRolls.equals(Arrays.asList(2, 3, 4, 5, 6))) {
            return 40;
        }
        return 0;

    }

    public int getScoreKniffel(List<Integer> diceRolls) {
        if (diceRolls.stream().collect(Collectors.groupingBy(d -> d)).values().stream().anyMatch(d -> d.size() == 5)) {
            return 50;
        }
        return 0;
    }

    public int getScoreChance(List<Integer> diceRolls) {
        return diceRolls.stream().mapToInt(i -> i).sum();
    }

}
