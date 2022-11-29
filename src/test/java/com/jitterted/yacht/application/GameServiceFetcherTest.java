package com.jitterted.yacht.application;

import com.jitterted.yacht.adapter.out.dieroller.DieRoller;
import com.jitterted.yacht.application.port.ScoreCategoryNotifier;
import com.jitterted.yacht.domain.ScoreCategory;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

class GameServiceFetcherTest {
    //    @Test
    void fetchesAverageForOneScoredCategory() {
        DiceRoller diceRoller = new DiceRoller(DieRoller.createNull(2, 3, 4, 5, 6));
        ScoreCategoryNotifier scoreCategoryNotifier = (diceRoll, score, scoreCategory) -> {
        };
        GameService gameService = new GameService(diceRoller, scoreCategoryNotifier);

        assertThat(gameService.averagesFor(List.of(ScoreCategory.BIGSTRAIGHT)))
                .containsEntry(ScoreCategory.BIGSTRAIGHT, 12.0);
    }
}