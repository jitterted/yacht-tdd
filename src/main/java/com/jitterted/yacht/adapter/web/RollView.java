package com.jitterted.yacht.adapter.web;

import com.jitterted.yacht.domain.DiceRoll;

import java.util.List;
import java.util.stream.Collectors;

public class RollView {

  public static List<Integer> listOf(DiceRoll diceRoll) {
    return diceRoll.stream().collect(Collectors.toList());
  }

  public static String forScoreboard(DiceRoll diceRoll) {
    return diceRoll
        .stream()
        .map(String::valueOf)
        .collect(Collectors.joining(" "));
  }
}
