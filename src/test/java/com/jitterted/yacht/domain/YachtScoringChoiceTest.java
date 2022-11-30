package com.jitterted.yacht.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

public class YachtScoringChoiceTest {

    @Test
    public void rollOfAnythingResultsInScoreOfSumOfDice() throws Exception {
        YachtScorer yachtScorer = new YachtScorer();

        int score = yachtScorer.scoreAsChoice(HandOfDice.of(3, 5, 4, 6, 6));

        assertThat(score)
                .isEqualTo(3 + 5 + 4 + 6 + 6);
    }

}
