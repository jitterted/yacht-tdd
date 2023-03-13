package com.jitterted.yacht.application;

import com.jitterted.yacht.adapter.out.averagescore.AverageScoreFetcher;
import com.jitterted.yacht.adapter.out.dieroller.DieRoller;
import com.jitterted.yacht.adapter.out.scorecategory.ScoreCategoryNotifier;
import com.jitterted.yacht.domain.ScoreCategory;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;

class GameServiceAverageFetcherTest {

    @Test
    void fetchesAverageForOneScoredCategory() {
        GameService gameService = createGameServiceWithAverageScoresOf(
                Map.of(ScoreCategory.BIGSTRAIGHT, 12.0));

        assertThat(gameService.averagesFor(List.of(ScoreCategory.BIGSTRAIGHT)))
                .containsEntry(ScoreCategory.BIGSTRAIGHT, 12.0);
    }

    @Test
    void fetchesAveragesForTwoScoredCategories() {
        GameService gameService = createGameServiceWithAverageScoresOf(
                Map.of(ScoreCategory.BIGSTRAIGHT, 12.0,
                       ScoreCategory.FOUROFAKIND, 20.0));

        List<ScoreCategory> scoreCategories = List.of(ScoreCategory.BIGSTRAIGHT,
                                                      ScoreCategory.FOUROFAKIND);

        assertThat(gameService.averagesFor(scoreCategories))
                .containsAllEntriesOf(Map.of(ScoreCategory.BIGSTRAIGHT, 12.0,
                                             ScoreCategory.FOUROFAKIND, 20.0));
    }

    private static GameService createGameServiceWithAverageScoresOf(Map<ScoreCategory, Double> map) {
        return new GameService(
                ScoreCategoryNotifier.createNull(),
                AverageScoreFetcher.createNull(map),
                DieRoller.createNull(),
                new InMemoryGameRepository());
    }

}