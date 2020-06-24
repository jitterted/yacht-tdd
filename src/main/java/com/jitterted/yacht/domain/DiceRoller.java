package com.jitterted.yacht.domain;

import java.util.ArrayList;
import java.util.List;

public class DiceRoller {
  private final DieRoller dieRoller;

  public DiceRoller() {
    dieRoller = new RandomDieRoller();
  }

  public DiceRoller(DieRoller dieRoller) {
    this.dieRoller = dieRoller;
  }

  public DiceRoll roll() {
    List<Integer> dieRolls = new ArrayList<>();
    for (int i = 0; i < 5; i++) {
      dieRolls.add(dieRoller.roll());
    }
    return DiceRoll.from(dieRolls);
  }
}
