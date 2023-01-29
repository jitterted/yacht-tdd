package com.jitterted.yacht.domain;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;

class GameMementoTest {

    @Test
    void mementoSavesAllGameState() {
        Game game = new Game();
        game.diceRolled(HandOfDice.of(1, 2, 3, 4, 5));
        game.assignRollTo(ScoreCategory.FIVES);

        Game.Memento memento = game.memento();

        assertThat(memento.roundCompleted())
                .isTrue();
        assertThat(memento.rolls())
                .isEqualTo(1);
        assertThat(memento.lastRoll())
                .containsExactly(1, 2, 3, 4, 5);
        assertThat(memento.scoreboard())
                .containsExactly(Map.entry(ScoreCategory.FIVES,
                                           List.of(1, 2, 3, 4, 5)));
    }

    @Test
    void createFromMementoRestoresAllGameState() {
        Game originalGame = new Game();
        originalGame.diceRolled(HandOfDice.of(3, 3, 4, 4, 5));
        originalGame.assignRollTo(ScoreCategory.FOURS);
        originalGame.diceRolled(HandOfDice.of(6, 6, 6, 6, 1));
        originalGame.diceReRolled(HandOfDice.of(6, 6, 6, 6, 6));

        Game.Memento originalGameMemento = originalGame.memento();

        Game restoredGame = Game.from(originalGameMemento);

        // NOTE: this assertion gives us nicer assertion failure messages,
        // but assumes all fields of Game are used for equality (which is true
        // at the time this was written)
        assertThat(restoredGame)
                .usingRecursiveComparison() // note: does not use Game.equals()
                .isEqualTo(originalGame);

        assertThat(restoredGame)
                .isEqualTo(originalGame);
    }
}