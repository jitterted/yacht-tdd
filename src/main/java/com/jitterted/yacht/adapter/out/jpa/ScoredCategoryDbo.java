package com.jitterted.yacht.adapter.out.jpa;

import com.jitterted.yacht.domain.ScoreCategory;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.List;

@Entity
public class ScoredCategoryDbo {
    @Id
    @GeneratedValue
    private Long id;

    private ScoreCategory scoreCategory;

    @ElementCollection
    private List<Integer> handOfDice;

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
