package com.jitterted.yacht.adapter.out.scorecategory;

import com.jitterted.yacht.domain.HandOfDice;
import com.jitterted.yacht.domain.ScoreCategory;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

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

        httpScoreCategoryNotifier.rollAssigned(HandOfDice.of(1, 3, 5, 2, 4),
                                               30,
                                               ScoreCategory.LITTLESTRAIGHT);

        HttpScoreCategoryNotifier.RollAssignedToCategory rollAssignedToCategory =
                httpScoreCategoryNotifier.lastRollAssigned();

        assertThat(rollAssignedToCategory.getCategory())
                .isEqualTo("LITTLESTRAIGHT");
        assertThat(rollAssignedToCategory.getScore())
                .isEqualTo("30");
        assertThat(rollAssignedToCategory.getRoll())
                .isEqualTo("1 3 5 2 4");
    }
}