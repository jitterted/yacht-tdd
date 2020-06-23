package com.jitterted.yacht.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

public class GameTest {

  @Test
  public void rollDiceThenQueryLastRollReturnsThatRoll() throws Exception {
    Game game = new Game();

    game.rollDice();

    assertThat(game.lastRoll())
        .isNotNull();
  }

  @Test
  public void newGameResultsInScoreOfZero() throws Exception {
    Game game = new Game();

    assertThat(game.score())
        .isZero();
  }

  @Test
  public void assign11234DiceRollToOnesScoringCategoryResultsInScoreOf2() throws Exception {
    Game game = new Game();

    game.rollDice();
    game.assignRollToNumberOnesCategory();

    assertThat(game.score())
        .isEqualTo(2);
  }

}
