package com.jitterted.yacht.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class HandOfDice {
    private static final HandOfDice NON_EXISTENT_DICE_ROLL = from(Collections.emptyList());
    private final List<Integer> dice;

    private HandOfDice(int die1, int die2, int die3, int die4, int die5) {
        dice = List.of(die1, die2, die3, die4, die5);
    }

    private HandOfDice(List<Integer> dieRolls) {
        dice = Collections.unmodifiableList(dieRolls);
    }

    public static HandOfDice of(int die1, int die2, int die3, int die4, int die5) {
        return new HandOfDice(die1, die2, die3, die4, die5);
    }

    public static HandOfDice from(List<Integer> dieRolls) {
        return from(dieRolls, () -> {
            throw new IllegalArgumentException("Invalid HandOfDice, dieRolls were: " + dieRolls);
        });
    }

    public static HandOfDice from(List<Integer> dieRolls, Supplier<HandOfDice> invalidResult) {
        if (isCorrectNumberOfDice(dieRolls)
                && hasValidDieRolls(dieRolls)) {
            return new HandOfDice(dieRolls);
        } else {
            return invalidResult.get();
        }
    }

    private static boolean isCorrectNumberOfDice(List<Integer> dieRolls) {
        return dieRolls.isEmpty() || dieRolls.size() == 5;
    }

    private static boolean hasValidDieRolls(List<Integer> dieRolls) {
        for (Integer integer : dieRolls) {
            if (integer < 1 || integer > 6) {
                return false;
            }
        }
        return true;
    }


    public static HandOfDice unassigned() {
        return NON_EXISTENT_DICE_ROLL;
    }

    public int countFor(int dieValue) {
        return (int) dice.stream()
                         .filter(die -> die == dieValue)
                         .count();
    }

    public Stream<Integer> stream() {
        return dice.stream();
    }

    @Override
    public String toString() {
        return "HandOfDice: " + dice;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        HandOfDice handOfDice = (HandOfDice) o;

        // compare without caring about the order
        var myDiceSorted = new ArrayList<>(dice);
        myDiceSorted.sort(Integer::compareTo);
        var otherDiceSorted = new ArrayList<>(handOfDice.dice);
        otherDiceSorted.sort(Integer::compareTo);

        return myDiceSorted.equals(otherDiceSorted);
    }

    @Override
    public int hashCode() {
        return dice.hashCode();
    }

    public int get(int index) {
        return dice.get(index);
    }

    boolean isEmpty() {
        return dice.isEmpty();
    }
}
