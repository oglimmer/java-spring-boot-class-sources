package com.oglimmer.kniffel;

import com.oglimmer.kniffel.logic.KniffelRulesImplementation;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

public class RuleTest {

    // an instance to test
    private KniffelRulesImplementation kniffelRules = new KniffelRulesImplementation();

    @Test
    public void test_threeOfKind_success() {
        List<Integer> diceRolls = Arrays.asList(1, 3, 6, 6, 6);
        int res = kniffelRules.getScoreForXofAKind(diceRolls, 3);
        Assertions.assertEquals(18, res);
    }

    @Test
    public void test_threeOfKind_success4Elements() {
        List<Integer> diceRolls = Arrays.asList(1, 1, 1, 1, 2);
        int res = kniffelRules.getScoreForXofAKind(diceRolls, 3);
        Assertions.assertEquals(3, res);
    }

    @Test
    public void test_threeOfKind_noPoints() {
        List<Integer> diceRolls = Arrays.asList(1, 3, 5, 6, 6);
        int res = kniffelRules.getScoreForXofAKind(diceRolls, 3);
        Assertions.assertEquals(0, res);
    }
}