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
    DiceRoll diceRoll = DiceRoll.of(6, 5, 4, 3, 2);
    Game game = new Game(new StubDiceRoller(diceRoll));

    game.rollDice();

    assertThat(game.lastRoll())
        .isEqualTo(diceRoll);
  }


}
