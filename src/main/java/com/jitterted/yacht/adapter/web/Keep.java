package com.jitterted.yacht.adapter.web;

import com.jitterted.yacht.domain.DiceRoll;

import java.util.List;
import java.util.stream.Collectors;

class Keep {
  private List<Integer> diceIndexesToKeep;

  public List<Integer> getDiceIndexesToKeep() {
    return diceIndexesToKeep;
  }

  public void setDiceIndexesToKeep(List<Integer> diceIndexesToKeep) {
    this.diceIndexesToKeep = diceIndexesToKeep;
  }

  public List<Integer> diceValuesFrom(DiceRoll diceRoll) {
    return diceIndexesToKeep.stream()
                            .map(diceRoll::get)
                            .collect(Collectors.toList());
  }
}
