package com.jitterted;

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

  public int scoreAsFives(String roll) {
    return calculateScore(roll, '5', 5);
  }

  private int calculateScore(String roll, char ch, int value) {
    int count = (int) (roll.chars()
                           .filter(c -> c == ch)
                           .count());
    return count * value;
  }
}
