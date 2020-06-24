package com.jitterted.yacht.domain;

public class DiceRoller {
  public DiceRoll roll() {
    return DiceRoll.of(1, 1, 2, 3, 4);
  }
}
