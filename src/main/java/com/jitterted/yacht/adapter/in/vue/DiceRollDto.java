package com.jitterted.yacht.adapter.in.vue;

import com.jitterted.yacht.adapter.in.web.RollView;
import com.jitterted.yacht.domain.HandOfDice;

import java.util.List;

public class DiceRollDto {
    private List<Integer> roll;

    private DiceRollDto(List<Integer> roll) {
        this.roll = roll;
    }

    public static DiceRollDto from(HandOfDice handOfDice) {
        return new DiceRollDto(RollView.listOf(handOfDice));
    }

    public List<Integer> getRoll() {
        return roll;
    }

    public void setRoll(List<Integer> roll) {
        this.roll = roll;
    }
}
