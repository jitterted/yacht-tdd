package com.jitterted.yacht;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

public class YachtScoringNumberCategoriesTest {

  @Test
  public void rollOf23456ResultsInScoreOfZeroForOnesCategory() throws Exception {
    Yacht yacht = new Yacht();

    int score = yacht.scoreAsOnes(List.of(2, 3, 4, 5, 6));

    assertThat(score)
        .isZero();
  }

  @Test
  public void rollOf12345ResultsInScoreOf1ForOnesCategory() throws Exception {
    Yacht yacht = new Yacht();

    int score = yacht.scoreAsOnes(List.of(1, 2, 3, 4, 5));

    assertThat(score)
        .isEqualTo(1);
  }

  @Test
  public void rollOf11111ResultsInScoreOf5ForOnesCategory() throws Exception {
    Yacht yacht = new Yacht();

    int score = yacht.scoreAsOnes(List.of(1, 1, 1, 1, 1));

    assertThat(score)
        .isEqualTo(5);
  }

  // ZOM/BIES - Zero/Empty, One, Many/More/More complex

  @Test
  public void rollOf12346Scores0ForFivesCategory() throws Exception {
    Yacht yacht = new Yacht();

    int score = yacht.scoreAsFives(List.of(1, 2, 3, 4, 6));

    assertThat(score)
        .isZero();
  }

  @Test
  public void rollOf13336Scores9ForThreesCategory() throws Exception {
    Yacht yacht = new Yacht();

    int score = yacht.scoreAsThrees(List.of(1, 3, 3, 3, 6));

    assertThat(score)
        .isEqualTo(9);
  }

  @Test
  public void rollOf12556Scores10ForFivesCategory() throws Exception {
    Yacht yacht = new Yacht();

    int score = yacht.scoreAsFives(List.of(1, 2, 5, 5, 6));

    assertThat(score)
        .isEqualTo(10);
  }

  @Test
  public void rollOf22345Scores4ForTwosCategory() throws Exception {
    Yacht yacht = new Yacht();

    int score = yacht.scoreAsTwos(List.of(2, 2, 3, 4, 5));

    assertThat(score)
        .isEqualTo(2 + 2);
  }

  @Test
  public void rollOf66644Scores18ForSixesCategory() throws Exception {
    Yacht yacht = new Yacht();

    int score = yacht.scoreAsSixes(List.of(6, 6, 6, 4, 4));

    assertThat(score)
        .isEqualTo(6 + 6 + 6);

  }

}
