package com.jitterted.yacht.domain;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class YachtScorer {

  public int scoreAsOnes(DiceRoll roll) {
    return calculateScore(roll, 1);
  }

  public int scoreAsTwos(DiceRoll roll) {
    return calculateScore(roll, 2);
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
    if (!isValidFullHouse(roll)) {
      return 0;
    }

    return roll.stream()
               .distinct()
               .mapToInt(die -> calculateScore(roll, die))
               .sum();
  }

  private boolean isValidFullHouse(DiceRoll roll) {
    var dieToCountMap = createDieToCountMap(roll);

    long numberOfDiceOccurringTwoOrThreeTimes = countForDieOccurringTwoOrThreeTimes(dieToCountMap);

    return hasTwoUniqueDice(dieToCountMap)
        && numberOfDiceOccurringTwoOrThreeTimes == 2;
  }

  private boolean hasTwoUniqueDice(Map<Integer, Long> dieToCountMap) {
    return dieToCountMap.size() == 2;
  }

  private long countForDieOccurringTwoOrThreeTimes(Map<Integer, Long> dieToCountMap) {
    return dieToCountMap.entrySet()
                 .stream()
                 .filter(this::twoOrThreeOccurrences)
                 .count();
  }

  private Map<Integer, Long> createDieToCountMap(DiceRoll roll) {
    return roll.stream()
        .collect(
            Collectors.groupingBy(
                Function.identity(),
                Collectors.counting()
            )
        );
  }

  private boolean twoOrThreeOccurrences(Map.Entry<Integer, Long> e) {
    return e.getValue() == 2 || e.getValue() == 3;
  }

  private int calculateScore(DiceRoll dice, int scoreCategory) {
    return dice.countFor(scoreCategory) * scoreCategory;
  }
}
