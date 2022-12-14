package com.jitterted.yacht.application;

import com.jitterted.yacht.adapter.out.averagescore.AverageScoreFetcher;
import com.jitterted.yacht.adapter.out.dieroller.DieRoller;
import com.jitterted.yacht.adapter.out.scorecategory.HttpScoreCategoryNotifier;
import com.jitterted.yacht.domain.ScoreCategory;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;

class GameServiceAverageFetcherTest {

    @Test
    void fetchesAverageForOneScoredCategory() {
        GameService gameService = new GameService(
                HttpScoreCategoryNotifier.createNull(),
                AverageScoreFetcher.createNull(
                        Map.of(ScoreCategory.BIGSTRAIGHT, 12.0)
                ),
                DieRoller.createNull());

        assertThat(gameService.averagesFor(List.of(ScoreCategory.BIGSTRAIGHT)))
                .containsEntry(ScoreCategory.BIGSTRAIGHT, 12.0);
    }

    @Test
    void fetchesAveragesForTwoScoredCategories() {
        GameService gameService = new GameService(
                HttpScoreCategoryNotifier.createNull(),
                AverageScoreFetcher.createNull(
                        Map.of(ScoreCategory.BIGSTRAIGHT, 12.0,
                               ScoreCategory.FOUROFAKIND, 20.0)
                ),
                DieRoller.createNull());

        List<ScoreCategory> scoreCategories = List.of(ScoreCategory.BIGSTRAIGHT,
                                                      ScoreCategory.FOUROFAKIND);

        assertThat(gameService.averagesFor(scoreCategories))
                .containsAllEntriesOf(Map.of(ScoreCategory.BIGSTRAIGHT, 12.0,
                                             ScoreCategory.FOUROFAKIND, 20.0));
    }

}