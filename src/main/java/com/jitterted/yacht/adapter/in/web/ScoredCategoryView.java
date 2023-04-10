package com.jitterted.yacht.adapter.in.web;

import com.jitterted.yacht.domain.ScoreCategory;
import com.jitterted.yacht.domain.ScoredCategory;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ScoredCategoryView {
    private final String description;
    private final String handOfDice;
    private final String score;
    private final String scoreAverage;
    private final boolean rollAssigned;

    public static ScoredCategoryView from(ScoredCategory scoredCategory, String scoreAverage) {
        return new ScoredCategoryView(scoredCategory.scoreCategory().toString(),
                                      RollView.forScoreboard(scoredCategory.handOfDice()),
                                      String.valueOf(scoredCategory.score()),
                                      scoredCategory.isAssigned(),
                                      scoreAverage);
    }

    public static List<ScoredCategoryView> viewOf(List<ScoredCategory> scoredCategories,
                                                  Map<ScoreCategory, Double> averages) {
        return scoredCategories.stream()
                               .sorted(Comparator.comparing(ScoredCategory::scoreCategory))
                               .map(scoredCategory -> from(scoredCategory, String.valueOf(averages.get(scoredCategory.scoreCategory()))))
                               .collect(Collectors.toList());
    }

    public ScoredCategoryView(String description, String handOfDice, String score, boolean rollAssigned, String scoreAverage) {
        this.description = description;
        this.handOfDice = handOfDice;
        this.score = score;
        this.scoreAverage = scoreAverage;
        this.rollAssigned = rollAssigned;
    }

    public boolean isRollAssigned() {
        return rollAssigned;
    }

    public String getDescription() {
        return description;
    }

    public String getHandOfDice() {
        return handOfDice;
    }

    public String getScore() {
        return score;
    }

    public String getScoreAverage() {
        return scoreAverage;
    }

    @Override
    public String toString() {
        return "ScoredCategoryView{" +
                "description='" + description + '\'' +
                ", handOfDice='" + handOfDice + '\'' +
                ", score='" + score + '\'' +
                ", scoreAverage='" + scoreAverage + '\'' +
                ", rollAssigned=" + rollAssigned +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ScoredCategoryView that = (ScoredCategoryView) o;

        if (rollAssigned != that.rollAssigned) {
            return false;
        }
        if (!description.equals(that.description)) {
            return false;
        }
        if (!handOfDice.equals(that.handOfDice)) {
            return false;
        }
        if (!score.equals(that.score)) {
            return false;
        }
        return scoreAverage.equals(that.scoreAverage);
    }

    @Override
    public int hashCode() {
        int result = description.hashCode();
        result = 31 * result + handOfDice.hashCode();
        result = 31 * result + score.hashCode();
        result = 31 * result + scoreAverage.hashCode();
        result = 31 * result + (rollAssigned ? 1 : 0);
        return result;
    }
}
