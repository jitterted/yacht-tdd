package com.jitterted;

public class Yacht {
  private final DieRoller dieRoller;

  public Yacht(DieRoller dieRoller) {
    this.dieRoller = dieRoller;
  }

  public String rollDice() {
    String result = "";
    for (int i = 0; i < 5; i++) {
      result += String.valueOf(dieRoller.roll());
    }
    return result;
  }

  public int scoreAsOnes(String roll) {
    int score = 0;
    for (int i = 0; i < roll.length(); i++) {
      if (roll.substring(i, i+1).equals("1")) {
        score++;
      }
    }
    return score;
  }
}
