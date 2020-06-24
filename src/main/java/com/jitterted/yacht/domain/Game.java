package com.jitterted.yacht.domain;

public class Game {
  private final YachtScorer yachtScorer = new YachtScorer();
  private final DiceRoller diceRoller;

  private DiceRoll lastRoll = new DiceRoll(0, 0, 0, 0, 0);

  private int score = 0;

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
    return score;
  }

  public void assignRollToNumberOnesCategory() {
    score = yachtScorer.scoreAsOnes(lastRoll);
  }

  public void assignRollToFullHouseCategory() {
    score = yachtScorer.scoreAsFullHouse(lastRoll);
  }

  public void assignRollToNumberSixesCategory() {
    score = yachtScorer.scoreAsSixes(lastRoll);
  }
}
