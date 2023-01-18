package com.jitterted.yacht.application;

import com.jitterted.yacht.adapter.OutputTracker;
import com.jitterted.yacht.adapter.out.averagescore.AverageScoreFetcher;
import com.jitterted.yacht.adapter.out.dieroller.DieRoller;
import com.jitterted.yacht.adapter.out.scorecategory.HttpScoreCategoryNotifier;
import com.jitterted.yacht.adapter.out.scorecategory.RollAssignment;
import com.jitterted.yacht.domain.HandOfDice;
import com.jitterted.yacht.domain.ScoreCategory;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;

class GameServiceNotificationTest {

    // TODO: Move to more appropriate test class
    @Test
    void canConfigureGameServiceWithDieRolls() {
        GameService.createNull(
                new GameService.NulledResponses()
                        .withDieRolls(List.of(1, 2, 3, 4, 5))
        fail("");
    }


    void canConfigureGameServiceWithAverageScores() {
        GameService.createNull(
                new GameService.NulledResponses()
                        .withAverageScores(Map.of(
                                ScoreCategory.FOURS, 13,
                                ScoreCategory.FIVES, 25,
                                ScoreCategory.FULLHOUSE, 6
                        )));
        fail("");
    }

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