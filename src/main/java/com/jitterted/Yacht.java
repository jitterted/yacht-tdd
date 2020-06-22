package com.jitterted;

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

  public int scoreAsFives(List<Integer> roll) {
    return calculateScore(roll, 5);
  }

  public int scoreAsFullHouse(List<Integer> roll) {
    Map<Integer, Long> listMap =
        roll.stream()
            .collect(
                Collectors.groupingBy(
                    Function.identity(),
                    Collectors.counting()
                )
            );

    long count = listMap.entrySet().stream()
                        .filter(e -> e.getValue() >= 2)
                        .count();

    if (count != 2) {
      return 0;
    }

    return roll.stream()
               .distinct()
               .mapToInt(die -> calculateScore(roll, die))
               .sum();
  }

  private int calculateScore(List<Integer> dice, int scoreCategory) {
    int count = (int) dice.stream()
                          .filter(die -> die == scoreCategory)
                          .count();
    return count * scoreCategory;
  }
}
