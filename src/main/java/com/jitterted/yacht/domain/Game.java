package com.jitterted.yacht.domain;

public class Game {
  private final YachtScorer yachtScorer = new YachtScorer();
  private DiceRoll lastRoll = new DiceRoll(0, 0, 0, 0, 0);

  private final DiceRoller diceRoller;

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
    return DiceRoll.of(1, 2, 3, 4, 5);
  }

  public int score() {
    return yachtScorer.scoreAsOnes(lastRoll);
  }

  public void assignRollToNumberOnesCategory() {

  }

  public void assignRollToFullHouseCategory() {

  }
}
