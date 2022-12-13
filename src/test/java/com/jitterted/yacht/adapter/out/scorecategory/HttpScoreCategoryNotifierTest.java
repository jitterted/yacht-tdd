package com.jitterted.yacht.adapter.out.scorecategory;

import com.jitterted.yacht.domain.HandOfDice;
import com.jitterted.yacht.domain.ScoreCategory;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

//@Tag("manual")
class HttpScoreCategoryNotifierTest {
    @Test
    @Disabled
    void rollAssignmentSentToTracker() {
        HttpScoreCategoryNotifier httpScoreCategoryNotifier =
                new HttpScoreCategoryNotifier();

        httpScoreCategoryNotifier.rollAssigned(HandOfDice.of(1, 3, 5, 2, 4),
                                               30,
                                               ScoreCategory.LITTLESTRAIGHT);
    }

    @Test
    void rollAssignmentIsTracked() {
        HttpScoreCategoryNotifier httpScoreCategoryNotifier =
                new HttpScoreCategoryNotifier();

        List<RollAssignment> tracker =
                httpScoreCategoryNotifier.trackAssignments();
        assertThat(tracker)
                .isEmpty();

        httpScoreCategoryNotifier.rollAssigned(HandOfDice.of(1, 3, 5, 2, 4),
                                               30,
                                               ScoreCategory.LITTLESTRAIGHT);

        assertThat(tracker)
                .containsOnly(new RollAssignment(
                        HandOfDice.of(1, 3, 5, 2, 4),
                        30,
                        ScoreCategory.LITTLESTRAIGHT));


        httpScoreCategoryNotifier.rollAssigned(HandOfDice.of(5, 5, 5, 5, 5),
                                               25,
                                               ScoreCategory.FIVES);

        assertThat(tracker)
                .containsOnly(new RollAssignment(
                                      HandOfDice.of(1, 3, 5, 2, 4),
                                      30,
                                      ScoreCategory.LITTLESTRAIGHT),
                              new RollAssignment(
                                      HandOfDice.of(5, 5, 5, 5, 5),
                                      25,
                                      ScoreCategory.FIVES));
    }


    @Test
    void rollAssignmentTrackingSupportsMultipleIndependentTrackers() {
        HttpScoreCategoryNotifier httpScoreCategoryNotifier =
                new HttpScoreCategoryNotifier();

        List<RollAssignment> tracker1 =
                httpScoreCategoryNotifier.trackAssignments();

        httpScoreCategoryNotifier.rollAssigned(HandOfDice.of(1, 3, 5, 2, 4),
                                               30,
                                               ScoreCategory.LITTLESTRAIGHT);
        List<RollAssignment> tracker2 =
                httpScoreCategoryNotifier.trackAssignments();

        httpScoreCategoryNotifier.rollAssigned(HandOfDice.of(5, 5, 5, 5, 5),
                                               25,
                                               ScoreCategory.FIVES);

        assertThat(tracker1)
                .containsOnly(new RollAssignment(
                                      HandOfDice.of(1, 3, 5, 2, 4),
                                      30,
                                      ScoreCategory.LITTLESTRAIGHT),
                              new RollAssignment(
                                      HandOfDice.of(5, 5, 5, 5, 5),
                                      25,
                                      ScoreCategory.FIVES));

        assertThat(tracker2)
                .containsOnly(new RollAssignment(
                        HandOfDice.of(5, 5, 5, 5, 5),
                        25,
                        ScoreCategory.FIVES));

    }
}