package com.jitterted;

import java.util.List;

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

  public int scoreAsOnes(String roll) {
    return calculateScore(roll, '1', 1);
  }


  public int scoreAsThrees(String roll) {
    return calculateScore(roll, '3', 3);
  }

  public int scoreAsFives(String roll) {
    return calculateScore(roll, '5', 5);
  }

  private int calculateScore(String roll, char dieSide, int scoreForDie) {
    int count = (int) (roll.chars()
                           .filter(c -> c == dieSide)
                           .count());
    return count * scoreForDie;
  }

  public int scoreAsFullHouse(String roll) {
    return 0;
  }

  public int scoreAsTwos(List<Integer> dice) {
    return calculateScore(dice, 2);
  }

  private int calculateScore(List<Integer> dice, int scoreCategory) {
    int count = (int) dice.stream()
                          .filter(die -> die == scoreCategory)
                          .count();
    return count * scoreCategory;
  }
}
