package com.jitterted.yacht.adapter.out.gamedatabase;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "games")
public class GameTable {
    @Id
    @GeneratedValue
    private Long id;

    private int rolls;
    
    private boolean roundCompleted;

    // couldn't get to work: https://vladmihalcea.com/postgresql-array-java-list/
    // private List<Integer> currentHand;
    private String currentHand;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public int getRolls() {
        return rolls;
    }

    public void setRolls(int rolls) {
        this.rolls = rolls;
    }

    public boolean isRoundCompleted() {
        return roundCompleted;
    }

    public void setRoundCompleted(boolean roundCompleted) {
        this.roundCompleted = roundCompleted;
    }

    public String getCurrentHand() {
        return currentHand;
    }

    public void setCurrentHand(String currentHand) {
        this.currentHand = currentHand;
    }
}
