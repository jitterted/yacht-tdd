package com.jitterted.yacht.domain;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Yacht {
  private final DieRoller dieRoller;

  public Yacht(DieRoller dieRoller) {
    this.dieRoller = dieRoller;
  }

  public Yacht() {
    this.dieRoller = new RandomDieRoller();
  }

  public String rollDice() {
    String result = "";
    for (int i = 0; i < 5; i++) {
      result += String.valueOf(dieRoller.roll());
    }
    return result;
  }

  // YAGNI - You Ain't Gonna Need It

  public int scoreAsOnes(List<Integer> roll) {
    return calculateScore(roll, 1);
  }

  public int scoreAsTwos(List<Integer> dice) {
    return calculateScore(dice, 2);
  }

  public int scoreAsThrees(List<Integer> roll) {
    return calculateScore(roll, 3);
  }

  public int scoreAsFours(List<Integer> roll) {
    return calculateScore(roll, 4);
  }

  public int scoreAsFives(List<Integer> roll) {
    return calculateScore(roll, 5);
  }

  public int scoreAsSixes(List<Integer> roll) {
    return calculateScore(roll, 6);
  }

  public int scoreAsFullHouse(List<Integer> roll) {
    if (isValidFullHouse(roll)) {
      return roll.stream()
                 .distinct()
                 .mapToInt(die -> calculateScore(roll, die))
                 .sum();
    }
    return 0;
  }

  private boolean isValidFullHouse(List<Integer> roll) {
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

  private int calculateScore(List<Integer> dice, int scoreCategory) {
    long count = dice.stream()
                     .filter(die -> die == scoreCategory)
                     .count();
    return (int) (count * scoreCategory);
  }
}
