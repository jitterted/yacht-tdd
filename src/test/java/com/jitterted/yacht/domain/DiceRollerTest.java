package com.jitterted.yacht.domain;

import org.junit.jupiter.api.Test;

import java.util.Iterator;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

public class DiceRollerTest {

    @Test
    public void multipleRollsShouldUseDieRoller() throws Exception {
        DiceRoller diceRoller = new DiceRoller(new DieRoller() {
            private final Iterator<Integer> dice = List.of(5, 1, 4, 2, 3).iterator();

            @Override
            public int roll() {
                return dice.next();
            }
        });

        assertThat(diceRoller.roll())
                .isEqualTo(DiceRoll.of(5, 1, 4, 2, 3));
    }

    @Test
    public void reRollKeepingTwoDiceResultsInNewRollIncludingKeptDice() throws Exception {
        DiceRoller diceRoller = new DiceRoller(new StubDieRoller(List.of(1, 2, 5)));
        List<Integer> keptDice = List.of(3, 3);

        DiceRoll diceRoll = diceRoller.reRoll(keptDice);

        assertThat(diceRoll)
                .isEqualTo(DiceRoll.of(3, 3, 1, 2, 5));
    }

    @Test
    public void reRollKeepingFourDiceResultsInNewRollIncludingKeptDice() throws Exception {
        DiceRoller diceRoller = new DiceRoller(new StubDieRoller(List.of(4)));
        List<Integer> keptDice = List.of(1, 1, 2, 2);

        DiceRoll diceRoll = diceRoller.reRoll(keptDice);

        assertThat(diceRoll)
                .isEqualTo(DiceRoll.of(1, 1, 2, 2, 4));
    }
}