package com.jitterted.yacht.adapter.out.scorecategory;

import com.jitterted.yacht.adapter.OutputTracker;
import com.jitterted.yacht.domain.HandOfDice;
import com.jitterted.yacht.domain.ScoreCategory;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

//@Tag("manual")
class HttpScoreCategoryNotifierTest {
    @Test
    @Disabled
    void rollAssignmentSentToRealExternalService() {
        HttpScoreCategoryNotifier httpScoreCategoryNotifier =
                HttpScoreCategoryNotifier.create();

        httpScoreCategoryNotifier.rollAssigned(HandOfDice.of(1, 3, 5, 2, 4),
                                               30,
                                               ScoreCategory.LITTLESTRAIGHT);
    }

    @Test
    void rollAssignmentIsTracked() {
        HttpScoreCategoryNotifier httpScoreCategoryNotifier =
                HttpScoreCategoryNotifier.createNull();

        OutputTracker<RollAssignment> tracker =
                httpScoreCategoryNotifier.trackAssignments();

        httpScoreCategoryNotifier.rollAssigned(HandOfDice.of(1, 3, 5, 2, 4),
                                               30,
                                               ScoreCategory.LITTLESTRAIGHT);

        assertThat(tracker.output())
                .containsOnly(new RollAssignment(
                        HandOfDice.of(1, 3, 5, 2, 4),
                        30,
                        ScoreCategory.LITTLESTRAIGHT));
    }

}