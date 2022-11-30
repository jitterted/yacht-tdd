package com.jitterted.yacht.domain;

import org.assertj.core.api.SoftAssertions;

import java.util.Arrays;
import java.util.function.Function;

// this was experimental, but takes about a full second to run
// perhaps due to the SoftAssertions, or something else?

class ScoreTester {
    public void testAllFourOfAKind() {
        YachtScorer yachtScorer = new YachtScorer();
        var fourOfAKind = ScoreTester.using(yachtScorer::scoreAsFourOfAKind);

        fourOfAKind.diceRollOf(3, 3, 3, 3, 6).scoresAs(3, 3, 3, 3);
        fourOfAKind.diceRollOf(5, 5, 5, 5, 6).scoresAs(5, 5, 5, 5);
        fourOfAKind.diceRollOf(6, 6, 6, 6, 6).scoresAs(6, 6, 6, 6);
        fourOfAKind.diceRollOf(1, 2, 3, 4, 5).scoresAs(0);
        fourOfAKind.diceRollOf(3, 3, 3, 4, 4).scoresAs(0);

        fourOfAKind.assertAll();
    }

    private final Function<HandOfDice, Integer> scorer;
    private final SoftAssertions softAssertions = new SoftAssertions();
    private HandOfDice handOfDice;

    public ScoreTester(Function<HandOfDice, Integer> scorer) {
        this.scorer = scorer;
    }

    public static ScoreTester using(Function<HandOfDice, Integer> scorer) {
        return new ScoreTester(scorer);
    }

    public ScoreTester diceRollOf(int i, int i1, int i2, int i3, int i4) {
        this.handOfDice = HandOfDice.of(i, i1, i2, i3, i4);
        return this;
    }

    public void scoresAs(int... values) {
        int sum = Arrays.stream(values).sum();
        softAssertions.assertThat(scorer.apply(handOfDice))
                      .isEqualTo(sum);
    }

    public void assertAll() {
        softAssertions.assertAll();
    }
}
