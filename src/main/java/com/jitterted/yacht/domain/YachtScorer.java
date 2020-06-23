package com.jitterted.yacht.domain;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class YachtScorer {

  public int scoreAsOnes(DiceRoll roll) {
    return calculateScore(roll, 1);
  }

  public int scoreAsTwos(DiceRoll dice) {
    return calculateScore(dice, 2);
  }

  public int scoreAsThrees(DiceRoll roll) {
    return calculateScore(roll, 3);
  }

  public int scoreAsFours(DiceRoll roll) {
    return calculateScore(roll, 4);
  }

  public int scoreAsFives(DiceRoll roll) {
    return calculateScore(roll, 5);
  }

  public int scoreAsSixes(DiceRoll roll) {
    return calculateScore(roll, 6);
  }

  public int scoreAsFullHouse(DiceRoll roll) {
    if (isValidFullHouse(roll)) {
      return roll.stream()
                 .distinct()
                 .mapToInt(die -> calculateScore(roll, die))
                 .sum();
    }
    return 0;
  }

  private boolean isValidFullHouse(DiceRoll roll) {
    Map<Integer, Long> dieToCountMap =
        roll.stream()
            .collect(
                Collectors.groupingBy(
                    Function.identity(),
                    Collectors.counting()
                )
            );

    long numberOfDiceOccurringTwoOrMoreTimes =
        dieToCountMap.entrySet()
                     .stream()
                     .filter(this::twoOrMoreOccurrences)
                     .count();

    return numberOfDiceOccurringTwoOrMoreTimes == 2;
  }

  private boolean twoOrMoreOccurrences(Map.Entry<Integer, Long> e) {
    return e.getValue() >= 2;
  }

  private int calculateScore(DiceRoll dice, int scoreCategory) {
    return dice.countFor(scoreCategory) * scoreCategory;
  }
}
