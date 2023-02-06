package com.jitterted.yacht.adapter.out.jpa;

import com.jitterted.yacht.domain.HandOfDice;
import com.jitterted.yacht.domain.ScoreCategory;
import com.jitterted.yacht.domain.Scoreboard;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.List;
import java.util.Map;

@Entity
public class ScoredCategoryDbo {
    @Id
    @GeneratedValue
    private Long id;

    private ScoreCategory scoreCategory;

    @ElementCollection
    private List<Integer> handOfDice;

    static List<ScoredCategoryDbo> fromEntry(Scoreboard.Snapshot scoreboard) {
        return scoreboard.scoredCategoryHandMap()
                         .entrySet()
                         .stream()
                         .map(ScoredCategoryDbo::fromEntry)
                         .toList();
    }

    private static ScoredCategoryDbo fromEntry(Map.Entry<ScoreCategory, HandOfDice> entry) {
        ScoredCategoryDbo dbo = new ScoredCategoryDbo();
        dbo.setScoreCategory(entry.getKey());
        dbo.setHandOfDice(entry.getValue().stream().toList());
        return dbo;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public ScoreCategory getScoreCategory() {
        return scoreCategory;
    }

    public void setScoreCategory(ScoreCategory scoreCategory) {
        this.scoreCategory = scoreCategory;
    }

    public List<Integer> getHandOfDice() {
        return handOfDice;
    }

    public void setHandOfDice(List<Integer> handOfDice) {
        this.handOfDice = handOfDice;
    }
}
