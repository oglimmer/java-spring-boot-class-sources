package com.oglimmer.kniffel.db.entity;

public enum GameState {
    /** the player has to select dice she/he wants to keep. Check the round if this is the first or second re-roll phase */
    ROLL, 
    /** the player has to book the dice on the table into one category */
    BOOK,
    /** all players did all their turns */
    ENDED
}
