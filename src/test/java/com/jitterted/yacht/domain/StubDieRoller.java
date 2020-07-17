package com.jitterted.yacht.domain;

import java.util.Iterator;
import java.util.List;

public class StubDieRoller implements DieRoller {
  private final Iterator<Integer> dieRolls;

  public StubDieRoller(List<Integer> dieRolls) {
    this.dieRolls = dieRolls.iterator();
  }

  @Override
  public int roll() {
    return dieRolls.next();
  }
}
