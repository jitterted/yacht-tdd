package com.jitterted.yacht.application;

import com.jitterted.yacht.adapter.out.dieroller.RandomDieRoller;
import com.jitterted.yacht.domain.DiceRoll;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

public class DiceRollerTest {

    @Test
    public void multipleRollsShouldUseDieRoller() throws Exception {
        RandomDieRoller dieRoller = RandomDieRoller.createNull(5, 1, 4, 2, 3);
        DiceRoller diceRoller = new DiceRoller(dieRoller);

        assertThat(diceRoller.roll())
                .isEqualTo(DiceRoll.of(5, 1, 4, 2, 3));
    }

    @Test
    public void reRollKeepingTwoDiceResultsInNewRollIncludingKeptDice() throws Exception {
        RandomDieRoller dieRoller = RandomDieRoller.createNull(1, 2, 5);
        DiceRoller diceRoller = new DiceRoller(dieRoller);
        List<Integer> keptDice = List.of(3, 3);

        DiceRoll diceRoll = diceRoller.reRoll(keptDice);

        assertThat(diceRoll)
                .isEqualTo(DiceRoll.of(3, 3, 1, 2, 5));
    }

    @Test
    public void reRollKeepingFourDiceResultsInNewRollIncludingKeptDice() throws Exception {
        DiceRoller diceRoller = new DiceRoller(RandomDieRoller.createNull(4));
        List<Integer> keptDice = List.of(1, 1, 2, 2);

        DiceRoll diceRoll = diceRoller.reRoll(keptDice);

        assertThat(diceRoll)
                .isEqualTo(DiceRoll.of(1, 1, 2, 2, 4));
    }
}