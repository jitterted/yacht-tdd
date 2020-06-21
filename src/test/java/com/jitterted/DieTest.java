package com.jitterted;

import org.junit.jupiter.api.RepeatedTest;

import static org.assertj.core.api.Assertions.*;

public class DieTest {

  @RepeatedTest(100)
  public void rollResultsInNumberBetween1and6() throws Exception {
    DieRoller dieRoller = new RandomDieRoller();

    int roll = dieRoller.roll();

    assertThat(roll)
        .isBetween(1, 6);
  }

}
