package com.jitterted.yacht.domain;

public class Game {
  private final DiceRoller diceRoller;
  private final Scoreboard scoreboard = new Scoreboard();

  private DiceRoll lastRoll = DiceRoll.of(0, 0, 0, 0, 0);

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
    return scoreboard.score();
  }

  public void assignRollTo(ScoreCategory scoreCategory) {
    scoreboard.scoreAs(scoreCategory, lastRoll);
  }
}
