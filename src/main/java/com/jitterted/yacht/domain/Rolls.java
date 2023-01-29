package com.jitterted.yacht.domain;

public class Rolls {
    private static final int MAX_NUMBER_OF_ROLLS_PER_TURN = 3;
    private int rolls;

    Rolls(int initialRolls) {
        rolls = initialRolls;
    }

    public static Rolls start() {
        return new Rolls(1);
    }

    public void increment() {
        rolls++;
    }

    public boolean canReRoll() {
        return rolls < MAX_NUMBER_OF_ROLLS_PER_TURN;
    }

    int rolls() {
        return rolls;
    }

    @Override
    public String toString() {
        return "Rolls = " + rolls;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Rolls rolls1 = (Rolls) o;

        return rolls == rolls1.rolls;
    }

    @Override
    public int hashCode() {
        return rolls;
    }
}
