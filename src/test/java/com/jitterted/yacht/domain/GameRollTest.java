package com.jitterted.yacht.domain;

import com.jitterted.yacht.adapter.out.dieroller.RandomDieRoller;
import com.jitterted.yacht.application.DiceRoller;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.assertj.core.api.Assertions.*;

public class GameRollTest {

    @Test
    public void afterInitialRollThenCanReRollIsTrue() throws Exception {
        Game game = new Game(new DiceRoller(new RandomDieRoller()));

        game.rollDice();

        assertThat(game.canReRoll())
                .isTrue();
    }

    @Test
    public void afterInitialRollAndOneReRollThenCanReRollIsTrue() throws Exception {
        Game game = new Game(new DiceRoller(new RandomDieRoller()));

        game.rollDice();
        game.reRoll(Collections.emptyList());

        assertThat(game.canReRoll())
                .isTrue();
    }

    @Test
    public void afterInitialRollAndTwoReRollsThenCanReRollIsFalse() throws Exception {
        Game game = new Game(new DiceRoller(new RandomDieRoller()));

        game.rollDice();
        game.reRoll(Collections.emptyList());
        game.reRoll(Collections.emptyList());

        assertThat(game.canReRoll())
                .isFalse();
    }

    @Test
    public void attemptToRollTotalOfFourTimesThrowsException() throws Exception {
        Game game = new Game(new DiceRoller(new RandomDieRoller()));

        game.rollDice();
        game.reRoll(Collections.emptyList());
        game.reRoll(Collections.emptyList());

        assertThatThrownBy(() -> {
            game.reRoll(Collections.emptyList());
        })
                .isInstanceOf(TooManyRollsException.class);
    }

}
