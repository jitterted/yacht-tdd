package com.jitterted.yacht.application;

import com.jitterted.yacht.adapter.OutputTracker;
import com.jitterted.yacht.adapter.out.averagescore.AverageScoreFetcher;
import com.jitterted.yacht.adapter.out.dieroller.DieRoller;
import com.jitterted.yacht.adapter.out.scorecategory.HttpScoreCategoryNotifier;
import com.jitterted.yacht.adapter.out.scorecategory.RollAssignment;
import com.jitterted.yacht.domain.HandOfDice;
import com.jitterted.yacht.domain.ScoreCategory;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class GameServiceNotificationTest {

    @Test
    void whenRollAssignedToCategoryNotificationIsSent() throws Exception {
        // GIVEN a started game and dice rolled
        DieRoller allSixesDieRoller = DieRoller.createNull(6, 6, 6, 6, 6);
        HttpScoreCategoryNotifier scoreCategoryNotifier = HttpScoreCategoryNotifier.createNull();
        OutputTracker<RollAssignment> tracker = scoreCategoryNotifier.trackAssignments();

        GameService gameService = new GameService(
                scoreCategoryNotifier,
                AverageScoreFetcher.createNull(),
                allSixesDieRoller);
        gameService.start();
        gameService.rollDice();

        // WHEN
        gameService.assignRollTo(ScoreCategory.SIXES);

        // THEN
        assertThat(tracker.output())
                .containsExactly(
                        new RollAssignment(HandOfDice.of(6, 6, 6, 6, 6),
                                           30,
                                           ScoreCategory.SIXES));
    }

}