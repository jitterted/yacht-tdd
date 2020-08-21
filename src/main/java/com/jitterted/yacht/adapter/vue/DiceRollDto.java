package com.jitterted.yacht.adapter.vue;

import com.jitterted.yacht.domain.DiceRoll;

public class DiceRollDto {
  private String roll;

  private DiceRollDto(String roll) {
    this.roll = roll;
  }

  public static DiceRollDto from(DiceRoll diceRoll) {
    return new DiceRollDto(diceRoll.toString());
  }

  public String getRoll() {
    return roll;
  }

  public void setRoll(String roll) {
    this.roll = roll;
  }
}
