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
  public void givenLastRollOf_11234_ScoreAsOnesCategoryResultsInScoreOf2() throws Exception {
    Game game = new Game(StubDiceRoller.createDiceRollerFor(1, 1, 2, 3, 4));

    game.rollDice();
    game.assignRollToNumberOnesCategory();

    assertThat(game.score())
        .isEqualTo(1 + 1);
  }

  @Test
  public void givenLastRollOf_44455_ScoreAsFullHouseResultsInScoreOf22() throws Exception {
    Game game = new Game(StubDiceRoller.createDiceRollerFor(4, 4, 4, 5, 5));

    game.rollDice();
    game.assignRollToFullHouseCategory();

    assertThat(game.score())
        .isEqualTo(4 + 4 + 4 + 5 + 5);
  }

  @Test
  public void givenLastRollOf_65456_ScoreAsSixesResultsInScoreOf12() throws Exception {
    Game game = new Game(StubDiceRoller.createDiceRollerFor(6, 5, 4, 5, 6));

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

  @Test
  public void twoRollsAssignedToDifferentCategoriesResultsInSumOfBothScores() throws Exception {
    DiceRoll fullHouseRoll = DiceRoll.of(1, 1, 1, 2, 2);
    DiceRoll sixesRoll = DiceRoll.of(6, 5, 1, 5, 6);
    StubDiceRoller diceRoller = new StubDiceRoller(fullHouseRoll, sixesRoll);
    Game game = new Game(diceRoller);

    game.rollDice();
    game.assignRollToFullHouseCategory();
    game.rollDice();
    game.assignRollToNumberSixesCategory();

    assertThat(game.score())
        .isEqualTo((1 + 1 + 1 + 2 + 2) + (6 + 6));
  }

}
