package com.jitterted.yacht.domain;

public class Game {
  private final YachtScorer yachtScorer = new YachtScorer();
  private DiceRoll lastRoll = new DiceRoll(0, 0, 0, 0, 0);

  public void rollDice() {
    lastRoll = DiceRoll.of(1, 1, 2, 3, 4);
  }

  public DiceRoll lastRoll() {
    return DiceRoll.of(1, 2, 3, 4, 5);
  }

  public int score() {
    return yachtScorer.scoreAsOnes(lastRoll);
  }

  public void assignRollToNumberOnesCategory() {

  }
}
