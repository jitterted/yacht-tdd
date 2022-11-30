package com.jitterted.yacht.adapter.out.scorecategory;

import com.jitterted.yacht.domain.HandOfDice;
import com.jitterted.yacht.domain.ScoreCategory;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("manual")
class HttpScoreCategoryNotifierTest {
    @Test
    void rollAssignmentSentToTracker() {
        HttpScoreCategoryNotifier httpScoreCategoryNotifier = new HttpScoreCategoryNotifier();

        httpScoreCategoryNotifier.rollAssigned(HandOfDice.of(1, 3, 5, 2, 4),
                                               30,
                                               ScoreCategory.LITTLESTRAIGHT);
    }
}