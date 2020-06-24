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
  public void givenLastRollOf_11234_ScoreAsOnesCategoryResultsInScoreOf2() throws Exception {
    Game game = new Game();

    game.rollDice();
    game.assignRollToNumberOnesCategory();

    assertThat(game.score())
        .isEqualTo(1 + 1);
  }

  @Test
  public void givenLastRollOf_44455_ScoreAsFullHouseResultsInScoreOf22() throws Exception {
    Game game = createGameWithDiceRollAlwaysOf(4, 4, 4, 5, 5);

    game.rollDice();
    game.assignRollToFullHouseCategory();

    assertThat(game.score())
        .isEqualTo(4 + 4 + 4 + 5 + 5);
  }

  @Test
  public void givenLastRollOf_65456_ScoreAsSixesResultsInScoreOf12() throws Exception {
    Game game = createGameWithDiceRollAlwaysOf(6, 5, 4, 5, 6);

    game.rollDice();
    game.assignRollToNumberSixesCategory();

    assertThat(game.score())
        .isEqualTo(6 + 6);
  }

  @Test
  public void lastRollReturnsValueOfMostRecentRollDice() throws Exception {
    DiceRoll diceRoll = DiceRoll.of(6, 5, 4, 3, 2);
    Game game = new Game(new StubDiceRoller(diceRoll));

    game.rollDice();

    assertThat(game.lastRoll())
        .isEqualTo(diceRoll);
  }

  private Game createGameWithDiceRollAlwaysOf(int die1, int die2, int die3, int die4, int die5) {
    return new Game(new StubDiceRoller(DiceRoll.of(die1, die2, die3, die4, die5)));
  }

  static class StubDiceRoller extends DiceRoller {

    private final DiceRoll diceRoll;

    public StubDiceRoller(DiceRoll diceRoll) {
      this.diceRoll = diceRoll;
    }

    @Override
    public DiceRoll roll() {
      return diceRoll;
    }
  }
}
