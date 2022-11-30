package com.jitterted.yacht.application;

import com.jitterted.yacht.domain.HandOfDice;

import java.util.List;
import java.util.stream.Collectors;

// QUESTIONABLE: where does this belong? Feels like a Value Object
public class Keep {
    private List<Integer> diceIndexesToKeep;

    public List<Integer> getDiceIndexesToKeep() {
        return diceIndexesToKeep;
    }

    public void setDiceIndexesToKeep(List<Integer> diceIndexesToKeep) {
        this.diceIndexesToKeep = diceIndexesToKeep;
    }

    public List<Integer> diceValuesFrom(HandOfDice handOfDice) {
        return diceIndexesToKeep.stream()
                                .map(handOfDice::get)
                                .collect(Collectors.toList());
    }
}
