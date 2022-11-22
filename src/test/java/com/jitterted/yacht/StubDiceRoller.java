package com.jitterted.yacht;

import com.jitterted.yacht.domain.DiceRoll;
import com.jitterted.yacht.domain.DiceRoller;

import java.util.Arrays;
import java.util.Iterator;

public class StubDiceRoller extends DiceRoller {

    private final Iterator<DiceRoll> diceRollIterator;

    public static StubDiceRoller createDiceRollerFor(int die1, int die2, int die3, int die4, int die5) {
        return new StubDiceRoller(DiceRoll.of(die1, die2, die3, die4, die5));
    }

    public StubDiceRoller(DiceRoll... diceRoll) {
        this.diceRollIterator = Arrays.asList(diceRoll).iterator();
    }

    @Override
    public DiceRoll roll() {
        return diceRollIterator.next();
    }
}
