package com.jitterted.yacht.domain;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.*;

class GameSnapshotTest {

    @Test
    void snapshotSavesAllGameState() {
        Game game = new Game();
        game.diceRolled(HandOfDice.of(1, 2, 3, 4, 5));
        game.assignCurrentHandTo(ScoreCategory.FIVES);

        Game.Snapshot snapshot = game.snapshot();

        assertThat(snapshot.roundCompleted())
                .isTrue();
        assertThat(snapshot.rolls())
                .isEqualTo(1);
        assertThat(snapshot.currentHand())
                .isEqualTo(HandOfDice.of(1, 2, 3, 4, 5));
        assertThat(snapshot.scoreboard().scoredCategoryHandMap())
                .containsExactly(Map.entry(ScoreCategory.FIVES,
                                           HandOfDice.of(1, 2, 3, 4, 5)));
    }

    @Test
    void createFromSnapshotRestoresAllGameState() {
        Game originalGame = new Game();
        originalGame.diceRolled(HandOfDice.of(3, 3, 4, 4, 5));
        originalGame.assignCurrentHandTo(ScoreCategory.FOURS);
        originalGame.diceRolled(HandOfDice.of(6, 6, 6, 6, 1));
        originalGame.diceReRolled(HandOfDice.of(6, 6, 6, 6, 6));

        Game.Snapshot originalGameSnapshot = originalGame.snapshot();

        Game restoredGame = Game.from(originalGameSnapshot);

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