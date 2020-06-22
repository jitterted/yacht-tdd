package com.jitterted.yacht.domain;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

public class YachtScoringFullHouseTest {

  @Test
  public void nonFullHouseRollScoresZero() throws Exception {
    Yacht yacht = new Yacht();

    int score = yacht.scoreAsFullHouse(List.of(1, 2, 3, 4, 5));

    assertThat(score)
        .isZero();
  }

  @Test
  public void rollOf33355ScoresAs19() throws Exception {
    Yacht yacht = new Yacht();

    int score = yacht.scoreAsFullHouse(List.of(3, 3, 3, 5, 5));

    assertThat(score)
        .isEqualTo(3 + 3 + 3 + 5 + 5);
  }

  @Test
  public void rollOf51111ScoreAs0() throws Exception {
    Yacht yacht = new Yacht();

    int score = yacht.scoreAsFullHouse(List.of(5, 1, 1, 1, 1));

    assertThat(score)
        .isZero();
  }

  @Test
  public void rollOf22666ScoresAs22() throws Exception {
    Yacht yacht = new Yacht();

    int score = yacht.scoreAsFullHouse(List.of(2, 2, 6, 6, 6));

    assertThat(score)
        .as("Should be scored as 2 + 2 + 6 + 6 + 6")
        .isEqualTo(2 + 2 + 6 + 6 + 6);
  }

  @Test
  public void rollOf55555ScoresAs0() throws Exception {
    Yacht yacht = new Yacht();

    int score = yacht.scoreAsFullHouse(List.of(5, 5, 5, 5, 5));

    assertThat(score)
        .isZero();
  }
}
