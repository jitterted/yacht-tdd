package com.jitterted.yacht.domain;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class YachtScorer {

  private static final DiceRoll LITTLE_STRAIGHT = DiceRoll.of(1, 2, 3, 4, 5);
  private static final DiceRoll BIG_STRAIGHT = DiceRoll.of(2, 3, 4, 5, 6);

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

  private Map<Integer, Long> createDieToCountMap(DiceRoll roll) {
    return roll.stream()
               .collect(
                   Collectors.groupingBy(
                       Function.identity(),
                       Collectors.counting()
                   )
               );
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

  private boolean twoOrThreeOccurrences(Map.Entry<Integer, Long> e) {
    return e.getValue() == 2 || e.getValue() == 3;
  }

  private int calculateScore(DiceRoll dice, int scoreCategory) {
    return dice.countFor(scoreCategory) * scoreCategory;
  }

  public int scoreAsFourOfAKind(DiceRoll diceRoll) {
    return createDieToCountMap(diceRoll).entrySet()
                                        .stream()
                                        .filter(this::fourOrFiveOccurrences)
                                        .mapToInt(this::multiplyDieValueTimesFour)
                                        .sum();
  }

  private boolean fourOrFiveOccurrences(Map.Entry<Integer, Long> die) {
    return die.getValue() >= 4;
  }

  private int multiplyDieValueTimesFour(Map.Entry<Integer, Long> die) {
    return die.getKey() * 4;
  }

  public int scoreAsLittleStraight(DiceRoll diceRoll) {
    return diceRoll.equals(LITTLE_STRAIGHT) ? 30 : 0;
  }

  public int scoreAsBigStraight(DiceRoll diceRoll) {
    return diceRoll.equals(BIG_STRAIGHT) ? 30 : 0;
  }
}
