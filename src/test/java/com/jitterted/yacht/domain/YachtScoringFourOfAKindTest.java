package com.jitterted.yacht.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

public class YachtScoringFourOfAKindTest {

    @Test
    public void rollOf33336ResultsIn12() throws Exception {
        YachtScorer yachtScorer = new YachtScorer();

        int score = yachtScorer.scoreAsFourOfAKind(HandOfDice.of(3, 3, 3, 3, 6));

        assertThat(score)
                .isEqualTo(3 + 3 + 3 + 3);
    }

    @Test
    public void rollOf65555ResultsIn20() throws Exception {
        YachtScorer yachtScorer = new YachtScorer();

        int score = yachtScorer.scoreAsFourOfAKind(HandOfDice.of(6, 5, 5, 5, 5));

        assertThat(score)
                .isEqualTo(5 + 5 + 5 + 5);
    }

    @Test
    public void rollOf12345ResultsIn0() throws Exception {
        YachtScorer yachtScorer = new YachtScorer();

        int score = yachtScorer.scoreAsFourOfAKind(HandOfDice.of(1, 2, 3, 4, 5));

        assertThat(score)
                .isZero();
    }

    @Test
    public void rollOf33344ResultsIn0() throws Exception {
        YachtScorer yachtScorer = new YachtScorer();

        int score = yachtScorer.scoreAsFourOfAKind(HandOfDice.of(3, 3, 3, 4, 4));

        assertThat(score)
                .isZero();
    }

    @Test
    public void rollOf66666ResultsIn24() throws Exception {
        YachtScorer yachtScorer = new YachtScorer();

        int score = yachtScorer.scoreAsFourOfAKind(HandOfDice.of(6, 6, 6, 6, 6));

        assertThat(score)
                .isEqualTo(6 + 6 + 6 + 6);
    }

}
