package com.jitterted.yacht.domain;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Scoreboard {

  private static final Map<ScoreCategory, Function<DiceRoll, Integer>> scorerMap = new EnumMap<>(ScoreCategory.class);

  private final Map<ScoreCategory, ScoredCategory> scoredCategoryMap = new HashMap<>();

  public Scoreboard() {
    populateScorerMap();
    populateScoredCategoriesMap();
  }

  private void populateScoredCategoriesMap() {
    for (ScoreCategory scoreCategory : ScoreCategory.values()) {
      scoredCategoryMap.put(scoreCategory,
                            ScoredCategory.createUnassignedScoredCategoryFor(scoreCategory));
    }
  }

  private void populateScorerMap() {
    YachtScorer yachtScorer = new YachtScorer();
    scorerMap.put(ScoreCategory.ONES, yachtScorer::scoreAsOnes);
    scorerMap.put(ScoreCategory.TWOS, yachtScorer::scoreAsTwos);
    scorerMap.put(ScoreCategory.THREES, yachtScorer::scoreAsThrees);
    scorerMap.put(ScoreCategory.FOURS, yachtScorer::scoreAsFours);
    scorerMap.put(ScoreCategory.FIVES, yachtScorer::scoreAsFives);
    scorerMap.put(ScoreCategory.SIXES, yachtScorer::scoreAsSixes);
    scorerMap.put(ScoreCategory.FULLHOUSE, yachtScorer::scoreAsFullHouse);
    scorerMap.put(ScoreCategory.FOUROFAKIND, yachtScorer::scoreAsFourOfAKind);
    scorerMap.put(ScoreCategory.LITTLESTRAIGHT, yachtScorer::scoreAsLittleStraight);
    scorerMap.put(ScoreCategory.BIGSTRAIGHT, yachtScorer::scoreAsBigStraight);
    scorerMap.put(ScoreCategory.CHOICE, yachtScorer::scoreAsChoice);
    scorerMap.put(ScoreCategory.YACHT, yachtScorer::scoreAsYacht);
  }

  public int score() {
    return scoredCategoryMap
        .values()
        .stream()
        .mapToInt(ScoredCategory::score)
        .sum();
  }

  public void scoreAs(ScoreCategory scoreCategory, DiceRoll diceRoll) {
    checkCategoryUnassigned(scoreCategory);
    scoredCategoryMap.put(scoreCategory,
                          new ScoredCategory(scoreCategory,
                                             diceRoll,
                                             scorerMap.get(scoreCategory).apply(diceRoll)));
  }

  public List<ScoredCategory> scoredCategories() {
    return Arrays.stream(ScoreCategory.values())
                 .map(this::scoredCategoryFor)
                 .collect(Collectors.toList());
  }

  public boolean isComplete() {
    return scoredCategoryMap.values()
                            .stream()
                            .allMatch(ScoredCategory::isAssigned);
  }

  public boolean isEmpty() {
    return scoredCategoryMap.values()
                            .stream()
                            .noneMatch(ScoredCategory::isAssigned);
  }

  private ScoredCategory scoredCategoryFor(ScoreCategory scoreCategory) {
    return scoredCategoryMap.get(scoreCategory);
  }

  private void checkCategoryUnassigned(ScoreCategory scoreCategory) {
    ScoredCategory scoredCategory = scoredCategoryMap.get(scoreCategory);
    if (scoredCategory.isAssigned()) {
      throw new IllegalStateException();
    }
  }

}
