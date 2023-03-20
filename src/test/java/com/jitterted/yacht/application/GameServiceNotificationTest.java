package com.jitterted.yacht.application;

import com.jitterted.yacht.adapter.OutputTracker;
import com.jitterted.yacht.adapter.out.averagescore.AverageScoreFetcher;
import com.jitterted.yacht.adapter.out.dieroller.DieRoller;
import com.jitterted.yacht.adapter.out.gamedatabase.GameDatabase;
import com.jitterted.yacht.adapter.out.scorecategory.RollAssignment;
import com.jitterted.yacht.adapter.out.scorecategory.ScoreCategoryNotifier;
import com.jitterted.yacht.domain.Game;
import com.jitterted.yacht.domain.HandOfDice;
import com.jitterted.yacht.domain.ScoreCategory;
import com.jitterted.yacht.domain.Scoreboard;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;

class GameServiceNotificationTest {

    // TODO: Move to more appropriate test class
    @Test
    void canConfigureGameServiceWithDieRolls() throws Exception {
        GameService gameService = GameService.createNull(
                new GameService.NulledResponses()
                        .withDieRolls(List.of(1, 2, 3, 4, 5)));
        gameService.start();
        gameService.rollDice();

        HandOfDice handOfDice = gameService.currentHand();

        assertThat(handOfDice)
                .isEqualTo(HandOfDice.of(1, 2, 3, 4, 5));
    }


    @Test
    void canConfigureGameServiceWithAverageScores() {
        Map<ScoreCategory, Double> expectedAverages = Map.of(
                ScoreCategory.FOURS, 13.0,
                ScoreCategory.FIVES, 25.0,
                ScoreCategory.FULLHOUSE, 6.0
        );
        GameService gameService = GameService.createNull(
                new GameService.NulledResponses()
                        .withAverageScores(expectedAverages));

        Map<ScoreCategory, Double> scoreCategoryDoubleMap =
                gameService.averagesFor(List.of(ScoreCategory.FOURS,
                                                ScoreCategory.FIVES,
                                                ScoreCategory.FULLHOUSE));

        assertThat(scoreCategoryDoubleMap)
                .isEqualTo(expectedAverages);
    }

    @Test
    void whenRollAssignedToCategoryNotificationIsSent() throws Exception {
        // GIVEN a started game and dice rolled
        ScoreCategoryNotifier scoreCategoryNotifier =
                ScoreCategoryNotifier.createNull();
        OutputTracker<RollAssignment> tracker =
                scoreCategoryNotifier.trackAssignments();

        GameService gameService = new GameService(
                scoreCategoryNotifier,
                AverageScoreFetcher.createNull(),
                DieRoller.createNull(),
                GameDatabase.createNull(
                        new Game.Snapshot(
                                1,
                                false,
                                HandOfDice.of(6, 6, 6, 6, 6),
                                new Scoreboard.Snapshot(Collections.emptyMap())
                        )
                ));

        // WHEN
        gameService.assignCurrentHandTo(ScoreCategory.SIXES);

        // THEN
        assertThat(tracker.output())
                .containsExactly(
                        new RollAssignment(HandOfDice.of(6, 6, 6, 6, 6),
                                           30,
                                           ScoreCategory.SIXES));
    }

}