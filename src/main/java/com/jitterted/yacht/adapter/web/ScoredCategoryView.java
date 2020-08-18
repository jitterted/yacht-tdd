package com.jitterted.yacht.adapter.web;

import com.jitterted.yacht.domain.ScoredCategory;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class ScoredCategoryView {
  private final String description;
  private final String diceRoll;
  private final String score;
  private final boolean rollAssigned;

  public static ScoredCategoryView from(ScoredCategory scoredCategory) {
    return new ScoredCategoryView(scoredCategory.scoreCategory().toString(),
                                  RollView.forScoreboard(scoredCategory.diceRoll()),
                                  String.valueOf(scoredCategory.score()),
                                  scoredCategory.isAssigned());
  }

  public ScoredCategoryView(String description, String diceRoll, String score, boolean rollAssigned) {
    this.description = description;
    this.diceRoll = diceRoll;
    this.score = score;
    this.rollAssigned = rollAssigned;
  }

  static List<ScoredCategoryView> viewOf(List<ScoredCategory> scoredCategories) {
    return scoredCategories.stream()
                           .sorted(Comparator.comparing(ScoredCategory::scoreCategory))
                           .map(ScoredCategoryView::from)
                           .collect(Collectors.toList());
  }

  public boolean isRollAssigned() {
    return rollAssigned;
  }

  public String getDescription() {
    return description;
  }

  public String getDiceRoll() {
    return diceRoll;
  }

  public String getScore() {
    return score;
  }

}
