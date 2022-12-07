package com.jitterted.yacht.application;

import com.jitterted.yacht.adapter.out.averagescore.HttpAverageScoreFetcher;
import com.jitterted.yacht.adapter.out.dieroller.DieRoller;
import com.jitterted.yacht.application.port.ScoreCategoryNotifier;
import com.jitterted.yacht.domain.HandOfDice;
import com.jitterted.yacht.domain.ScoreCategory;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class GameServiceNotificationTest {

    @Test
    void whenRollAssignedToCategoryNotificationIsSent() throws Exception {
        ScoreCategoryNotifierMock scoreCategoryNotifierMock =
                new ScoreCategoryNotifierMock();

        // GIVEN a started game and dice rolled
        DieRoller allSixesDieRoller = DieRoller.createNull(6, 6, 6, 6, 6);
        GameService gameService = new GameService(
                scoreCategoryNotifierMock,
                HttpAverageScoreFetcher.createNull(),
                allSixesDieRoller);
        gameService.start();
        gameService.rollDice();

        // WHEN
        gameService.assignRollTo(ScoreCategory.SIXES);

        // THEN
        scoreCategoryNotifierMock.verifyScoreSent();
    }

    private static class ScoreCategoryNotifierMock implements ScoreCategoryNotifier {
        private boolean rollAssigned;

        @Override
        public void rollAssigned(HandOfDice handOfDice, int score, ScoreCategory scoreCategory) {
            assertThat(handOfDice)
                    .isEqualTo(HandOfDice.of(6, 6, 6, 6, 6));
            assertThat(score)
                    .isEqualTo(30);
            assertThat(scoreCategory)
                    .isEqualTo(ScoreCategory.SIXES);
            rollAssigned = true;
        }

        public void verifyScoreSent() {
            assertThat(rollAssigned)
                    .describedAs("rollAssigned() was not called.")
                    .isTrue();
        }

    }
}