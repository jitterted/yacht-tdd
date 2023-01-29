package com.jitterted.yacht.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

public class GameTest {

    @Test
    public void newGameResultsInScoreOfZero() throws Exception {
        Game game = new Game();

        assertThat(game.score())
                .isZero();
    }

    @Test
    public void lastRollReturnsValueOfMostRecentRollDice() throws Exception {
        Game game = new Game();

        game.diceRolled(HandOfDice.of(2, 3, 4, 5, 6));

        assertThat(game.currentHand())
                .isEqualTo(HandOfDice.of(2, 3, 4, 5, 6));
    }

}
