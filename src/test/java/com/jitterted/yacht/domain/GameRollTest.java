package com.jitterted.yacht.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

public class GameRollTest {

    private static final DiceRoll DICE_ROLE_2_3_4_5_6 = DiceRoll.of(2, 3, 4, 5, 6);

    @Test
    public void afterInitialRollThenCanReRollIsTrue() throws Exception {
        Game game = new Game();

        game.rollDice(DICE_ROLE_2_3_4_5_6);

        assertThat(game.canReRoll())
                .isTrue();
    }

    @Test
    public void afterInitialRollAndOneReRollThenCanReRollIsTrue() throws Exception {
        Game game = new Game();

        game.rollDice(DICE_ROLE_2_3_4_5_6);
        game.reRoll(DICE_ROLE_2_3_4_5_6);

        assertThat(game.canReRoll())
                .isTrue();
    }

    @Test
    public void afterInitialRollAndTwoReRollsThenCanReRollIsFalse() throws Exception {
        Game game = new Game();

        game.rollDice(DICE_ROLE_2_3_4_5_6);
        game.reRoll(DICE_ROLE_2_3_4_5_6);
        game.reRoll(DICE_ROLE_2_3_4_5_6);

        assertThat(game.canReRoll())
                .isFalse();
    }

    @Test
    public void attemptToRollTotalOfFourTimesThrowsException() throws Exception {
        Game game = new Game();

        game.rollDice(DICE_ROLE_2_3_4_5_6);
        game.reRoll(DICE_ROLE_2_3_4_5_6);
        game.reRoll(DICE_ROLE_2_3_4_5_6);

        assertThatThrownBy(() -> {
            game.reRoll(DICE_ROLE_2_3_4_5_6);
        })
                .isInstanceOf(TooManyRollsException.class);
    }

}
