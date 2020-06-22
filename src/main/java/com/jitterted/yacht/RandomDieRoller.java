package com.jitterted.yacht;

import java.util.Random;

public class RandomDieRoller implements DieRoller {

  private final Random random = new Random();

  @Override
  public int roll() {
    return random.nextInt(6) + 1;
  }
}
