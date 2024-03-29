package com.jitterted.yacht.adapter.in.web;

import com.jitterted.yacht.domain.HandOfDice;
import com.jitterted.yacht.domain.ScoreCategory;
import com.jitterted.yacht.domain.ScoredCategory;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;

class ScoredCategoryViewTest {

    @Test
    void hasNoAveragesWhenNoCategoriesHaveBeenScored() {
        List<ScoredCategory> scoredCategories = Collections.emptyList();

        List<ScoredCategoryView> scoredCategoryViews =
                ScoredCategoryView.viewOf(scoredCategories, Collections.emptyMap());

        assertThat(scoredCategoryViews)
                .isEmpty();
    }

    @Test
    public void hasOneAverageForOneScoredCategory() throws Exception {
        List<ScoredCategory> scoredCategories = List.of(
                new ScoredCategory(ScoreCategory.THREES,
                                   HandOfDice.of(1, 2, 3, 3, 3),
                                   9));

        List<ScoredCategoryView> scoredCategoryViews =
                ScoredCategoryView.viewOf(scoredCategories,
                                          Map.of(ScoreCategory.THREES, 8.0));

        assertThat(scoredCategoryViews)
                .extracting(ScoredCategoryView::getScoreAverage)
                .containsExactly("8.0");
    }

    @Test
    public void twoScoredCategoriesBothHaveAverageScores() throws Exception {
        ScoredCategory scoredCategoryThrees =
                new ScoredCategory(ScoreCategory.THREES,
                                   HandOfDice.of(1, 2, 3, 3, 3),
                                   9);
        ScoredCategory scoredCategoryFives =
                new ScoredCategory(ScoreCategory.FIVES,
                                   HandOfDice.of(1, 5, 5, 3, 3),
                                   10);
        List<ScoredCategory> scoredCategories = List.of(
                scoredCategoryThrees, scoredCategoryFives);
        Map<ScoreCategory, Double> averagesMap =
                Map.of(ScoreCategory.THREES, 9.0,
                       ScoreCategory.FIVES, 10.0);

        List<ScoredCategoryView> scoredCategoryViews = ScoredCategoryView.viewOf(scoredCategories, averagesMap);

        assertThat(scoredCategoryViews)
                .extracting(ScoredCategoryView::getScoreAverage)
                .containsExactly("9.0", "10.0");
    }
}