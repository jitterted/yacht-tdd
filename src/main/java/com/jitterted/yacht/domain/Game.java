package com.jitterted.yacht.domain;

import java.util.List;

public class Game {
  private static final int MAX_NUMBER_OF_ROLLS_PER_TURN = 3;
  private final DiceRoller diceRoller;
  private final Scoreboard scoreboard = new Scoreboard();

  private DiceRoll lastRoll = DiceRoll.of(0, 0, 0, 0, 0);

  private int numberOfRolls;
  private boolean lastRollAssignedToCategory;

  public Game() {
    diceRoller = new DiceRoller();
  }

  public Game(DiceRoller diceRoller) {
    this.diceRoller = diceRoller;
  }

  public void rollDice() {
    lastRollAssignedToCategory = false;
    numberOfRolls = 1;
    lastRoll = diceRoller.roll();
  }

  public void reRoll(List<Integer> keptDice) {
    numberOfRolls++;
    lastRoll = diceRoller.reRoll(keptDice);
  }

  public DiceRoll lastRoll() {
    return lastRoll;
  }

  public int score() {
    return scoreboard.score();
  }

  public void assignRollTo(ScoreCategory scoreCategory) {
    lastRollAssignedToCategory = true;
    scoreboard.scoreAs(scoreCategory, lastRoll);
  }

  public List<ScoredCategory> scoredCategories() {
    return scoreboard.scoredCategories();
  }

  public boolean canReRoll() {
    if (lastRollAssignedToCategory()) {
      return false;
    }
    return numberOfRolls < MAX_NUMBER_OF_ROLLS_PER_TURN;
  }

  public boolean lastRollAssignedToCategory() {
    return lastRollAssignedToCategory;
  }
}
