package com.jitterted.yacht.domain;

import java.util.List;

public class Game {

  private final DiceRoller diceRoller;
  private Scoreboard scoreboard;

  private DiceRoll lastRoll = DiceRoll.of(0, 0, 0, 0, 0);

  private Rolls rolls = Rolls.start();
  private boolean roundCompleted;

  public Game() {
    this(new DiceRoller());
  }

  public Game(DiceRoller diceRoller) {
    this.scoreboard = new Scoreboard();
    this.diceRoller = diceRoller;
  }

  public void rollDice() {
    roundCompleted = false;
    rolls = Rolls.start();
    lastRoll = diceRoller.roll();
  }

  public void reRoll(List<Integer> keptDice) {
    rolls.increment();
    lastRoll = diceRoller.reRoll(keptDice);
  }

  public DiceRoll lastRoll() {
    return lastRoll;
  }

  public int score() {
    return scoreboard.score();
  }

  public void assignRollTo(ScoreCategory scoreCategory) {
    scoreboard.scoreAs(scoreCategory, lastRoll);
    roundCompleted = true;
  }

  public List<ScoredCategory> scoredCategories() {
    return scoreboard.scoredCategories();
  }

  public boolean canReRoll() {
    if (roundCompleted()) {
      return false;
    }
    return rolls.canReRoll();
  }

  public boolean roundCompleted() {
    return roundCompleted;
  }

  public boolean isOver() {
    return scoreboard.isComplete();
  }

  public void start() {
    scoreboard = new Scoreboard();
    roundCompleted = true;
  }
}
