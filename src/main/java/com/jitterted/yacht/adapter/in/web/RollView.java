package com.jitterted.yacht.adapter.in.web;

import com.jitterted.yacht.domain.HandOfDice;

import java.util.List;
import java.util.stream.Collectors;

public class RollView {

    public static List<Integer> listOf(HandOfDice handOfDice) {
        return handOfDice.stream().collect(Collectors.toList());
    }

    public static String forScoreboard(HandOfDice handOfDice) {
        return handOfDice
                .stream()
                .map(String::valueOf)
                .collect(Collectors.joining(" "));
    }
}
