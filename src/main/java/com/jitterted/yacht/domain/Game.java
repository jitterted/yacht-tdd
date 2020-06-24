package com.jitterted.yacht.domain;

public class Game {
  private final YachtScorer yachtScorer = new YachtScorer();
  private final DiceRoller diceRoller;

  private DiceRoll lastRoll = new DiceRoll(0, 0, 0, 0, 0);

  private boolean isAssignedToOnesCategory = false;
  private boolean isAssignedToSixesCategory = false;

  public Game() {
    diceRoller = new DiceRoller();
  }

  public Game(DiceRoller diceRoller) {
    this.diceRoller = diceRoller;
  }

  public void rollDice() {
    lastRoll = diceRoller.roll();
  }

  public DiceRoll lastRoll() {
    return lastRoll;
  }

  public int score() {
    if (isAssignedToOnesCategory) {
      return yachtScorer.scoreAsOnes(lastRoll);
    } else if (isAssignedToSixesCategory) {
      return yachtScorer.scoreAsSixes(lastRoll);
    } else {
      return yachtScorer.scoreAsFullHouse(lastRoll);
    }
  }

  public void assignRollToNumberOnesCategory() {
    isAssignedToOnesCategory = true;
  }

  public void assignRollToFullHouseCategory() {
    isAssignedToOnesCategory = false;
  }

  public void assignRollToNumberSixesCategory() {
    isAssignedToOnesCategory = false;
    isAssignedToSixesCategory = true;
  }
}
