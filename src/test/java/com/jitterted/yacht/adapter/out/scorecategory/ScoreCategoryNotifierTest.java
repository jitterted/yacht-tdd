package com.jitterted.yacht.adapter.out.scorecategory;

import com.jitterted.yacht.adapter.OutputTracker;
import com.jitterted.yacht.adapter.out.JsonHttpClient;
import com.jitterted.yacht.adapter.out.JsonHttpRequest;
import com.jitterted.yacht.domain.HandOfDice;
import com.jitterted.yacht.domain.ScoreCategory;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class ScoreCategoryNotifierTest {
    @Test
    void rollAssignmentDoesPostToNotifierService() {
        JsonHttpClient jsonHttpClient = JsonHttpClient.createNull();

        OutputTracker<JsonHttpRequest> tracker =
                jsonHttpClient.trackRequests();

        ScoreCategoryNotifier scoreCategoryNotifier =
                new ScoreCategoryNotifier(jsonHttpClient);

        scoreCategoryNotifier.rollAssigned(HandOfDice.of(1, 3, 5, 2, 4),
                                           30,
                                           ScoreCategory.LITTLESTRAIGHT);

        assertThat(tracker.output())
                .containsOnly(JsonHttpRequest.createPost(
                        "http://localhost:8080/api/scores",
                        RollAssignedToCategory.from(HandOfDice.of(1, 3, 5, 2, 4),
                                                   30,
                                                   ScoreCategory.LITTLESTRAIGHT)));
    }

    @Test
    void rollAssignmentIsTracked() {
        ScoreCategoryNotifier scoreCategoryNotifier =
                ScoreCategoryNotifier.createNull();

        OutputTracker<RollAssignment> tracker =
                scoreCategoryNotifier.trackAssignments();

        scoreCategoryNotifier.rollAssigned(HandOfDice.of(1, 3, 5, 2, 4),
                                           30,
                                           ScoreCategory.LITTLESTRAIGHT);

        assertThat(tracker.output())
                .containsOnly(new RollAssignment(
                        HandOfDice.of(1, 3, 5, 2, 4),
                        30,
                        ScoreCategory.LITTLESTRAIGHT));
    }

}