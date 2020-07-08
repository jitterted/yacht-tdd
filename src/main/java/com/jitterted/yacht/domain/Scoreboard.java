package com.jitterted.yacht.domain;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Scoreboard {

  private final Map<ScoreCategory, Function<DiceRoll, Integer>> scorerMap = new EnumMap<>(ScoreCategory.class);

  private final Map<ScoreCategory, DiceRoll> scoredCategories = new HashMap<>();

  public Scoreboard() {
    YachtScorer yachtScorer = new YachtScorer();
    scorerMap.put(ScoreCategory.ONES, yachtScorer::scoreAsOnes);
    scorerMap.put(ScoreCategory.TWOS, yachtScorer::scoreAsTwos);
    scorerMap.put(ScoreCategory.THREES, yachtScorer::scoreAsThrees);
    scorerMap.put(ScoreCategory.FOURS, yachtScorer::scoreAsFours);
    scorerMap.put(ScoreCategory.FIVES, yachtScorer::scoreAsFives);
    scorerMap.put(ScoreCategory.SIXES, yachtScorer::scoreAsSixes);
    scorerMap.put(ScoreCategory.FULLHOUSE, yachtScorer::scoreAsFullHouse);
  }

  public int score() {
    return scoredCategories
        .entrySet()
        .stream()
        .mapToInt(this::scoreFor)
        .sum();
  }

  public void scoreAs(ScoreCategory scoreCategory, DiceRoll diceRoll) {
    scoredCategories.put(scoreCategory, diceRoll);
  }

  public List<ScoredCategory> scoredCategories() {
    return scoredCategories.entrySet()
                           .stream()
                           .map((this::scoredCategoryFrom))
                           .collect(Collectors.toList());
  }

  private ScoredCategory scoredCategoryFrom(Map.Entry<ScoreCategory, DiceRoll> entry) {
    return new ScoredCategory(entry.getKey(),
                              entry.getValue(),
                              scoreFor(entry));
  }

  private int scoreFor(Map.Entry<ScoreCategory, DiceRoll> entry) {
    return scorerMap.get(entry.getKey()).apply(entry.getValue());
  }
}
