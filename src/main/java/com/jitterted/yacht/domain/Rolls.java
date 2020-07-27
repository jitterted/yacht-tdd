package com.jitterted.yacht.domain;

public class Rolls {
  private static final int MAX_NUMBER_OF_ROLLS_PER_TURN = 3;
  private int rolls;

  private Rolls(int initialRolls) {
    rolls = initialRolls;
  }

  public static Rolls start() {
    return new Rolls(1);
  }

  public void increment() {
    rolls++;
  }

  public boolean canReRoll() {
    return rolls < MAX_NUMBER_OF_ROLLS_PER_TURN;
  }
}
