package com.jitterted.yacht.domain;

public class ScoredCategory {
  private final ScoreCategory scoreCategory;
  private final DiceRoll diceRoll;
  private final int score;

  public ScoredCategory(ScoreCategory scoreCategory, DiceRoll diceRoll, int score) {
    this.scoreCategory = scoreCategory;
    this.diceRoll = diceRoll;
    this.score = score;
  }

  public ScoreCategory scoreCategory() {
    return scoreCategory;
  }

  public DiceRoll diceRoll() {
    return diceRoll;
  }

  public int score() {
    return score;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    ScoredCategory that = (ScoredCategory) o;

    if (score != that.score) return false;
    if (scoreCategory != that.scoreCategory) return false;
    return diceRoll.equals(that.diceRoll);
  }

  @Override
  public int hashCode() {
    int result = scoreCategory.hashCode();
    result = 31 * result + diceRoll.hashCode();
    result = 31 * result + score;
    return result;
  }
}
