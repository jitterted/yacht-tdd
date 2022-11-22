package com.jitterted.yacht.adapter.vue;

import com.jitterted.yacht.adapter.web.ScoredCategoryView;

import java.util.List;

public class ScoreCategoriesDto {
    private final int totalScore;
    private final List<ScoredCategoryView> categories;

    public ScoreCategoriesDto(int totalScore, List<ScoredCategoryView> categories) {
        this.totalScore = totalScore;
        this.categories = categories;
    }

    public int getTotalScore() {
        return totalScore;
    }

    public List<ScoredCategoryView> getCategories() {
        return categories;
    }
}
