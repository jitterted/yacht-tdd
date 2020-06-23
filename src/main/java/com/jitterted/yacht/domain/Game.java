package com.jitterted.yacht.domain;

import java.util.Collections;
import java.util.List;

public class Game {
  private final YachtScorer yachtScorer = new YachtScorer();
  private List<Integer> lastRoll = Collections.emptyList();

  public void rollDice() {
    lastRoll = List.of(1, 1, 2, 3, 4);
  }

  public List<Integer> lastRoll() {
    return List.of(1, 2, 3, 4, 5);
  }

  public int score() {
    return yachtScorer.scoreAsOnes(lastRoll);
  }

  public void assignRollToNumberOnesCategory() {

  }
}
