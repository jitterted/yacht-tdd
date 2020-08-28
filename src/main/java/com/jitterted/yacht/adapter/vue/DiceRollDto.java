package com.jitterted.yacht.adapter.vue;

import com.jitterted.yacht.adapter.web.RollView;
import com.jitterted.yacht.domain.DiceRoll;

import java.util.List;

public class DiceRollDto {
  private List<Integer> roll;

  private DiceRollDto(List<Integer> roll) {
    this.roll = roll;
  }

  public static DiceRollDto from(DiceRoll diceRoll) {
    return new DiceRollDto(RollView.listOf(diceRoll));
  }

  public List<Integer> getRoll() {
    return roll;
  }

  public void setRoll(List<Integer> roll) {
    this.roll = roll;
  }
}
