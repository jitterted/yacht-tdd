package com.jitterted.yacht.domain;


import java.util.Arrays;

public interface GameEvent {

    record DiceRolled() implements GameEvent {
    }

    record Started() implements GameEvent {
    }

    record DiceRerolled(Integer... dice) implements GameEvent {
        @Override
        public String toString() {
            return "DiceRerolled {" +
                    "dice=" + Arrays.toString(dice) +
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

            DiceRerolled that = (DiceRerolled) o;

            return Arrays.equals(dice, that.dice);
        }

        @Override
        public int hashCode() {
            return Arrays.hashCode(dice);
        }
    }

    record CategoryAssigned(ScoreCategory scoreCategory) implements GameEvent {
    }
}