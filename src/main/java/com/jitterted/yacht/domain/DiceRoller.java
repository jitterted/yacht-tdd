package com.jitterted.yacht.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DiceRoller {
  private static final int YACHT_DICE_COUNT = 5;
  private final DieRoller dieRoller;

  public DiceRoller() {
    dieRoller = new RandomDieRoller();
  }

  public DiceRoller(DieRoller dieRoller) {
    this.dieRoller = dieRoller;
  }

  public DiceRoll roll() {
    return reRoll(Collections.emptyList());
  }

  public DiceRoll reRoll(List<Integer> keptDice) {
    List<Integer> dieRolls = new ArrayList<>();
    dieRolls.addAll(keptDice);
    rollTheRest(dieRolls);
    return DiceRoll.from(dieRolls);
  }

  private void rollTheRest(List<Integer> dieRolls) {
    while (dieRolls.size() < YACHT_DICE_COUNT) {
      dieRolls.add(dieRoller.roll());
    }
  }

}
