package com.jitterted.yacht.domain;

import com.jitterted.yacht.adapter.out.dieroller.RandomDieRoller;
import com.jitterted.yacht.application.DiceRoller;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.assertj.core.api.Assertions.*;

public class GameRollTest {

    @Test
    public void afterInitialRollThenCanReRollIsTrue() throws Exception {
        DiceRoller diceRoller = new DiceRoller(new RandomDieRoller());
        Game game = new Game(diceRoller);

        game.rollDice(diceRoller.roll());

        assertThat(game.canReRoll())
                .isTrue();
    }

    @Test
    public void afterInitialRollAndOneReRollThenCanReRollIsTrue() throws Exception {
        DiceRoller diceRoller = new DiceRoller(new RandomDieRoller());
        Game game = new Game(diceRoller);

        game.rollDice(diceRoller.roll());
        game.reRoll(Collections.emptyList());

        assertThat(game.canReRoll())
                .isTrue();
    }

    @Test
    public void afterInitialRollAndTwoReRollsThenCanReRollIsFalse() throws Exception {
        DiceRoller diceRoller = new DiceRoller(new RandomDieRoller());
        Game game = new Game(diceRoller);

        game.rollDice(diceRoller.roll());
        game.reRoll(Collections.emptyList());
        game.reRoll(Collections.emptyList());

        assertThat(game.canReRoll())
                .isFalse();
    }

    @Test
    public void attemptToRollTotalOfFourTimesThrowsException() throws Exception {
        DiceRoller diceRoller = new DiceRoller(new RandomDieRoller());
        Game game = new Game(diceRoller);

        game.rollDice(diceRoller.roll());
        game.reRoll(Collections.emptyList());
        game.reRoll(Collections.emptyList());

        assertThatThrownBy(() -> {
            game.reRoll(Collections.emptyList());
        })
                .isInstanceOf(TooManyRollsException.class);
    }

}
