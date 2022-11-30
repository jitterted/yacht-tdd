package com.jitterted.yacht.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

public class GameRollTest {

    private static final HandOfDice DICE_ROLE_2_3_4_5_6 = HandOfDice.of(2, 3, 4, 5, 6);

    @Test
    public void afterInitialRollThenCanReRollIsTrue() throws Exception {
        Game game = new Game();

        game.diceRolled(DICE_ROLE_2_3_4_5_6);

        assertThat(game.canReRoll())
                .isTrue();
    }

    @Test
    public void afterInitialRollAndOneReRollThenCanReRollIsTrue() throws Exception {
        Game game = new Game();

        game.diceRolled(DICE_ROLE_2_3_4_5_6);
        game.diceReRolled(DICE_ROLE_2_3_4_5_6);

        assertThat(game.canReRoll())
                .isTrue();
    }

    @Test
    public void afterInitialRollAndTwoReRollsThenCanReRollIsFalse() throws Exception {
        Game game = new Game();

        game.diceRolled(DICE_ROLE_2_3_4_5_6);
        game.diceReRolled(DICE_ROLE_2_3_4_5_6);
        game.diceReRolled(DICE_ROLE_2_3_4_5_6);

        assertThat(game.canReRoll())
                .isFalse();
    }

    @Test
    public void attemptToRollTotalOfFourTimesThrowsException() throws Exception {
        Game game = new Game();

        game.diceRolled(DICE_ROLE_2_3_4_5_6);
        game.diceReRolled(DICE_ROLE_2_3_4_5_6);
        game.diceReRolled(DICE_ROLE_2_3_4_5_6);

        assertThatThrownBy(() -> {
            game.diceReRolled(DICE_ROLE_2_3_4_5_6);
        })
                .isInstanceOf(TooManyRollsException.class);
    }

}
