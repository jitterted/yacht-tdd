package com.jitterted.yacht.domain;

import org.junit.jupiter.api.Test;

import java.util.Iterator;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

public class DiceRollerTest {

  @Test
  public void multipleRollsShouldUseDieRoller() throws Exception {
    DiceRoller diceRoller = new DiceRoller(new DieRoller() {
      private final Iterator<Integer> dice = List.of(5, 1, 4, 2, 3).iterator();

      @Override
      public int roll() {
        return dice.next();
      }
    });

    assertThat(diceRoller.roll())
        .isEqualTo(DiceRoll.of(5, 1, 4, 2, 3));
  }
}