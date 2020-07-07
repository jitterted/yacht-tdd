package com.jitterted.yacht.domain;

public class Scoreboard {
  private int score = 0;
  private final YachtScorer yachtScorer = new YachtScorer();

  public int score() {
    return score;
  }

  public void scoreAsOnes(DiceRoll lastRoll) {
    score += yachtScorer.scoreAsOnes(lastRoll);
  }

  public void scoreAsFullHouse(DiceRoll lastRoll) {
    score += yachtScorer.scoreAsFullHouse(lastRoll);
  }

  public void scoreAsSixes(DiceRoll lastRoll) {
    score += yachtScorer.scoreAsSixes(lastRoll);
  }

  public void scoreAsThrees(DiceRoll lastRoll) {
    score += yachtScorer.scoreAsThrees(lastRoll);
  }

  public void scoreAs(ScoreCategory scoreCategory, DiceRoll lastRoll) {
    if (scoreCategory == ScoreCategory.THREES) {
      scoreAsThrees(lastRoll);
    } else if (scoreCategory == ScoreCategory.ONES) {
      scoreAsOnes(lastRoll);
    } else if (scoreCategory == ScoreCategory.SIXES) {
      scoreAsSixes(lastRoll);
    } else {
      scoreAsFullHouse(lastRoll);
    }
  }
}
