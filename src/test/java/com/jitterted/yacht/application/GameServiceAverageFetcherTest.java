package com.jitterted.yacht.application;

import com.jitterted.yacht.adapter.out.dieroller.DieRoller;
import com.jitterted.yacht.application.port.ScoreCategoryNotifier;
import com.jitterted.yacht.domain.ScoreCategory;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;

class GameServiceAverageFetcherTest {

    private static final ScoreCategoryNotifier NO_OP_SCORE_CATEGORY_NOTIFIER = (diceRoll, score, scoreCategory) -> {
    };

    @Test
    void fetchesAverageForOneScoredCategory() {
        DiceRoller diceRoller = new DiceRoller(DieRoller.create());
        GameService gameService = new GameService(diceRoller,
                                                  NO_OP_SCORE_CATEGORY_NOTIFIER,
                                                  new DefaultAverageScoreFetcher());

        assertThat(gameService.averagesFor(List.of(ScoreCategory.BIGSTRAIGHT)))
                .containsEntry(ScoreCategory.BIGSTRAIGHT, 12.0);
    }

    @Test
    void fetchesAveragesForTwoScoredCategories() {
        DiceRoller diceRoller = new DiceRoller(DieRoller.create());
        GameService gameService = new GameService(diceRoller,
                                                  NO_OP_SCORE_CATEGORY_NOTIFIER,
                                                  new DefaultAverageScoreFetcher());

        List<ScoreCategory> scoreCategories = List.of(ScoreCategory.BIGSTRAIGHT,
                                                      ScoreCategory.FOUROFAKIND);

        assertThat(gameService.averagesFor(scoreCategories))
                .containsAllEntriesOf(Map.of(ScoreCategory.BIGSTRAIGHT, 12.0,
                                             ScoreCategory.FOUROFAKIND, 20.0));
    }
}