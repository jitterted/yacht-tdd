package com.jitterted.yacht.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

public class GameCategoryAssignmentTest {

  private static final ScoreCategory ARBITRARY_SCORE_CATEGORY = ScoreCategory.FULLHOUSE;

  @Test
  public void newRollThenLastRollIsNotYetAssignedToCategory() throws Exception {
    Game game = new Game();
    game.rollDice();

    assertThat(game.roundCompleted())
        .isFalse();
  }

  @Test
  public void newRollWhenAssignedThenRollIsAssignedToCategory() throws Exception {
    Game game = new Game();
    game.rollDice();

    game.assignRollTo(ARBITRARY_SCORE_CATEGORY);

    assertThat(game.roundCompleted())
        .isTrue();
  }

  @Test
  public void newRollAfterAssignmentWhenRollAgainThenRollIsNotAssignedToCategory() throws Exception {
    Game game = new Game();
    game.rollDice();
    game.assignRollTo(ARBITRARY_SCORE_CATEGORY);

    game.rollDice();

    assertThat(game.roundCompleted())
        .isFalse();
  }

  @Test
  public void newRollAfterAssignmentThenShouldNotBeAbleToReRoll() throws Exception {
    Game game = new Game();
    game.rollDice();

    game.assignRollTo(ARBITRARY_SCORE_CATEGORY);

    assertThat(game.canReRoll())
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

  private void assignRollToAllCategories(Game game) {
    for (ScoreCategory scoreCategory : ScoreCategory.values()) {
      game.rollDice();
      game.assignRollTo(scoreCategory);
    }
  }
}
