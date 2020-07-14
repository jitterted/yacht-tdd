package com.jitterted.yacht.adapter.web;

import com.jitterted.yacht.domain.ScoredCategory;

public class ScoredCategoryView {
  private String description;
  private String diceRoll;
  private String score;

  public static ScoredCategoryView from(ScoredCategory scoredCategory) {
    return new ScoredCategoryView(scoredCategory.scoreCategory().toString(),
                                  RollView.asOneString(scoredCategory.diceRoll()),
                                  String.valueOf(scoredCategory.score()));
  }

  public ScoredCategoryView(String description, String diceRoll, String score) {
    this.description = description;
    this.diceRoll = diceRoll;
    this.score = score;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getDiceRoll() {
    return diceRoll;
  }

  public void setDiceRoll(String diceRoll) {
    this.diceRoll = diceRoll;
  }

  public String getScore() {
    return score;
  }

  public void setScore(String score) {
    this.score = score;
  }
}
