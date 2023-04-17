package com.jitterted.yacht.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

public class GameCategoryAssignmentTest {

    private static final ScoreCategory ARBITRARY_SCORE_CATEGORY = ScoreCategory.FULLHOUSE;
    private static final HandOfDice DICE_ROLL_1_2_3_4_5 = HandOfDice.of(1, 2, 3, 4, 5);

    @Test
    public void newRollThenLastRollIsNotYetAssignedToCategory() throws Exception {
        Game game = new Game();
        game.diceRolled(DICE_ROLL_1_2_3_4_5);

        assertThat(game.roundCompleted())
                .isFalse();
    }

    @Test
    public void newRollWhenAssignedThenRollIsAssignedToCategory() throws Exception {
        Game game = new Game();
        game.diceRolled(DICE_ROLL_1_2_3_4_5);

        game.assignCurrentHandTo(ARBITRARY_SCORE_CATEGORY);

        assertThat(game.roundCompleted())
                .isTrue();
    }

    @Test
    public void newRollAfterAssignmentWhenRollAgainThenRollIsNotAssignedToCategory() throws Exception {
        Game game = new Game();
        game.diceRolled(DICE_ROLL_1_2_3_4_5);
        game.assignCurrentHandTo(ARBITRARY_SCORE_CATEGORY);

        game.diceRolled(DICE_ROLL_1_2_3_4_5);

        assertThat(game.roundCompleted())
                .isFalse();
    }

    @Test
    public void newRollAfterAssignmentThenShouldNotBeAbleToReRoll() throws Exception {
        Game game = new Game();
        game.diceRolled(DICE_ROLL_1_2_3_4_5);

        game.assignCurrentHandTo(ARBITRARY_SCORE_CATEGORY);

        assertThat(game.canReroll())
                .isFalse();
    }

    @Test
    public void newGameThenGameIsNotOver() throws Exception {
        Game game = new Game();

        assertThat(game.isOver())
                .isFalse();
    }

    @Test
    public void assigningToAllCategoriesEndsTheGame() throws Exception {
        Game game = new Game();

        assignRollToAllCategories(game);

        assertThat(game.isOver())
                .isTrue();
    }

    @Test
    public void assignedCategoryCanNotBeAssignedToAgain() throws Exception {
        Game game = new Game();
        game.diceRolled(DICE_ROLL_1_2_3_4_5);
        game.assignCurrentHandTo(ARBITRARY_SCORE_CATEGORY);
        game.diceRolled(DICE_ROLL_1_2_3_4_5);

        assertThatThrownBy(() -> game.assignCurrentHandTo(ARBITRARY_SCORE_CATEGORY))
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void assignedRollCanNotBeReAssigned() throws Exception {
        Game game = new Game();
        game.diceRolled(DICE_ROLL_1_2_3_4_5);
        game.assignCurrentHandTo(ScoreCategory.FOURS);

        assertThatThrownBy(() -> {
            game.assignCurrentHandTo(ScoreCategory.FULLHOUSE);
        }).isInstanceOf(IllegalStateException.class);
    }

    private void assignRollToAllCategories(Game game) {
        for (ScoreCategory scoreCategory : ScoreCategory.values()) {
            game.diceRolled(DICE_ROLL_1_2_3_4_5);
            game.assignCurrentHandTo(scoreCategory);
        }
    }
}
