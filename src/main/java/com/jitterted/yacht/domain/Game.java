package com.jitterted.yacht.domain;

public class Game {
  private final DiceRoller diceRoller;

  private DiceRoll lastRoll = DiceRoll.of(0, 0, 0, 0, 0);
  private Scoreboard scoreboard = new Scoreboard();

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

  public void assignRollToNumberOnesCategory() {
    scoreboard.scoreAsOnes(lastRoll);
  }

  public void assignRollToFullHouseCategory() {
    scoreboard.scoreAsFullHouse(lastRoll);
  }

  public void assignRollToNumberSixesCategory() {
    scoreboard.scoreAsSixes(lastRoll);
  }

  public void assignRollTo(ScoreCategory scoreCategory) {
    scoreboard.scoreAs(scoreCategory, lastRoll);
  }
}
