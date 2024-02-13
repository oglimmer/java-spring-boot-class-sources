package com.oglimmer.kniffel.db.entity;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RemoveDiceTest {

    @Test
    public void testRemoveDice_simplest_removeAll() {
        List<Integer> diceRolls = new ArrayList<>(Arrays.asList(1));
        KniffelGame game = new KniffelGame();
        game.removeDice(diceRolls, null);
        Assertions.assertArrayEquals(new Integer[0], diceRolls.toArray());
    }

    @Test
    public void testRemoveDice_simplest_keepAll() {
        List<Integer> diceRolls = new ArrayList<>(Arrays.asList(1));
        KniffelGame game = new KniffelGame();
        game.removeDice(diceRolls, new int[]{1});
        Assertions.assertArrayEquals(new Integer[]{1}, diceRolls.toArray());
    }

    @Test
    public void testRemoveDice_2ele_removeAll() {
        List<Integer> diceRolls = new ArrayList<>(Arrays.asList(1, 2));
        KniffelGame game = new KniffelGame();
        game.removeDice(diceRolls, null);
        Assertions.assertArrayEquals(new Integer[0], diceRolls.toArray());
    }

    @Test
    public void testRemoveDice_2ele_keepAll() {
        List<Integer> diceRolls = new ArrayList<>(Arrays.asList(1, 2));
        KniffelGame game = new KniffelGame();
        game.removeDice(diceRolls, new int[]{1, 2});
        Assertions.assertArrayEquals(new Integer[]{1, 2}, diceRolls.toArray());
    }

    @Test
    public void testRemoveDice_6ele_keepHalf() {
        List<Integer> diceRolls = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5, 6));
        int[] diceToKeep = new int[]{1, 2, 3};
        KniffelGame game = new KniffelGame();
        game.removeDice(diceRolls, diceToKeep);
        Assertions.assertArrayEquals(new Integer[]{1, 2, 3}, diceRolls.toArray());
    }

    @Test
    public void testRemoveDice_6ele_keepSome() {
        List<Integer> diceRolls = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5, 6));
        int[] diceToKeep = new int[]{1, 3, 5};
        KniffelGame game = new KniffelGame();
        game.removeDice(diceRolls, diceToKeep);
        Assertions.assertArrayEquals(new Integer[]{1, 3, 5}, diceRolls.toArray());
    }

    @Test
    public void testRemoveDice_6ele_keepAll() {
        List<Integer> diceRolls = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5, 6));
        int[] diceToKeep = new int[]{1, 2, 3, 4, 5, 6};
        KniffelGame game = new KniffelGame();
        game.removeDice(diceRolls, diceToKeep);
        Assertions.assertArrayEquals(new Integer[]{1, 2, 3, 4, 5, 6}, diceRolls.toArray());
    }

    @Test
    public void testRemoveDice_duplicatedEle_keepNone() {
        List<Integer> diceRolls = new ArrayList<>(Arrays.asList(1, 1));
        int[] diceToKeep = new int[0];
        KniffelGame game = new KniffelGame();
        game.removeDice(diceRolls, diceToKeep);
        Assertions.assertArrayEquals(new Integer[]{}, diceRolls.toArray());
    }

    @Test
    public void testRemoveDice_duplicatedEle_keepHalf() {
        List<Integer> diceRolls = new ArrayList<>(Arrays.asList(1, 1));
        int[] diceToKeep = new int[]{1};
        KniffelGame game = new KniffelGame();
        game.removeDice(diceRolls, diceToKeep);
        Assertions.assertArrayEquals(new Integer[]{1}, diceRolls.toArray());
    }

    @Test
    public void testRemoveDice_duplicatedEle_keepAll() {
        List<Integer> diceRolls = new ArrayList<>(Arrays.asList(1, 1));
        int[] diceToKeep = new int[]{1, 1};
        KniffelGame game = new KniffelGame();
        game.removeDice(diceRolls, diceToKeep);
        Assertions.assertArrayEquals(new Integer[]{1, 1}, diceRolls.toArray());
    }

    @Test
    public void testRemoveDice_complex_keepAll() {
        List<Integer> diceRolls = new ArrayList<>(Arrays.asList(6, 4, 2, 4, 4, 2, 3, 1));
        int[] diceToKeep = new int[]{6, 4, 2, 4, 4, 2, 3, 1};
        KniffelGame game = new KniffelGame();
        game.removeDice(diceRolls, diceToKeep);
        Assertions.assertArrayEquals(new Integer[]{6, 4, 2, 4, 4, 2, 3, 1}, diceRolls.toArray());
    }

    @Test
    public void testRemoveDice_complex_keepNone() {
        List<Integer> diceRolls = new ArrayList<>(Arrays.asList(6, 4, 2, 4, 4, 2, 3, 1));
        int[] diceToKeep = new int[]{};
        KniffelGame game = new KniffelGame();
        game.removeDice(diceRolls, diceToKeep);
        Assertions.assertArrayEquals(new Integer[]{}, diceRolls.toArray());
    }

    @Test
    public void testRemoveDice_complex_keepOne() {
        List<Integer> diceRolls = new ArrayList<>(Arrays.asList(6, 4, 2, 4, 4, 2, 3, 1));
        int[] diceToKeep = new int[]{4};
        KniffelGame game = new KniffelGame();
        game.removeDice(diceRolls, diceToKeep);
        Assertions.assertArrayEquals(new Integer[]{4}, diceRolls.toArray());
    }

    @Test
    public void testRemoveDice_complex_keepOne2() {
        List<Integer> diceRolls = new ArrayList<>(Arrays.asList(6, 4, 2, 4, 4, 2, 3, 1));
        int[] diceToKeep = new int[]{1};
        KniffelGame game = new KniffelGame();
        game.removeDice(diceRolls, diceToKeep);
        Assertions.assertArrayEquals(new Integer[]{1}, diceRolls.toArray());
    }

    @Test
    public void testRemoveDice_complex_keepSome() {
        List<Integer> diceRolls = new ArrayList<>(Arrays.asList(6, 4, 2, 4, 4, 2, 3, 1));
        int[] diceToKeep = new int[]{1, 3, 2, 4};
        KniffelGame game = new KniffelGame();
        game.removeDice(diceRolls, diceToKeep);
        Assertions.assertArrayEquals(new Integer[]{4, 2, 3, 1}, diceRolls.toArray());
    }

}
