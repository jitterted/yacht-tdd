package com.jitterted;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

public class YachtScoringFullHouseTest {

  @Test
  public void nonFullHouseRollScoresZero() throws Exception {
    Yacht yacht = new Yacht();

    int score = yacht.scoreAsFullHouse("12345");

    assertThat(score)
        .isZero();
  }

  @Disabled
  public void rollOf33355ScoresAs19() throws Exception {
    Yacht yacht = new Yacht();

    int score = yacht.scoreAsFullHouse("33355");

    assertThat(score)
        .isEqualTo(3 + 3 + 3 + 5 + 5);
  }

  @Disabled
  public void rollOf22666ScoresAs22() throws Exception {
    Yacht yacht = new Yacht();

    int score = yacht.scoreAsFullHouse("22666");

    assertThat(score)
        .as("Should be scored as 2 + 2 + 6 + 6 + 6")
        .isEqualTo(2 + 2 + 6 + 6 + 6);
  }

  @Disabled("Not Yet!")
  public void rollOf55555ScoresAs0() throws Exception {

  }
}
