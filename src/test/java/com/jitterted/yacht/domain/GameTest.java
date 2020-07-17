package com.jitterted.yacht.domain;

import com.jitterted.yacht.StubDiceRoller;
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
  public void lastRollReturnsValueOfMostRecentRollDice() throws Exception {
    Game game = new Game(new StubDiceRoller(DiceRoll.of(6, 5, 4, 3, 2)));

    game.rollDice();

    assertThat(game.lastRoll())
        .isEqualTo(DiceRoll.of(2, 3, 4, 5, 6));
  }

}
