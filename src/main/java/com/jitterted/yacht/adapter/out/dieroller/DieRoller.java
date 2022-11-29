package com.jitterted.yacht.adapter.out.dieroller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Random;

public class DieRoller {

    private final RandomInt random;

    private DieRoller(RandomInt randomInt) {
        this.random = randomInt;
    }

    public static DieRoller create() {
        return new DieRoller(new RandomWrapper());
    }

    public static DieRoller createNull(Integer... values) {
        return new DieRoller(new RandomStub(values));
    }

    public int roll() {
        return random.nextInt(6) + 1;
    }

    public List<Integer> rollMultiple(int numDice) {
        List<Integer> dice = new ArrayList<>();
        for (int i = 0; i < numDice; i++) {
            dice.add(roll());
        }
        return dice;
    }


    // ---- Nullable stuff below


    private interface RandomInt {
        int nextInt(int bound);
    }

    private static class RandomWrapper implements RandomInt {
        private final Random random;

        public RandomWrapper() {
            this.random = new Random();
        }

        @Override
        public int nextInt(int bound) {
            return this.random.nextInt(bound);
        }
    }

    private static class RandomStub implements RandomInt {
        private final List<Integer> values;
        private int current = 0;

        public RandomStub(Integer... values) {
            this.values = new ArrayList<>(Arrays.asList(values));
        }

        @Override
        public int nextInt(int bound) {
            if (values.isEmpty()) {
                return 0;
            } else {
                requireHaveMoreConfiguredRolls();
                int value = values.get(current++);
                requireWithinRange(value);
                return value - 1;
            }
        }

        private void requireHaveMoreConfiguredRolls() {
            if (current >= values.size()) {
                throw new NoSuchElementException("No more rolls configured in Null DieRoller.");
            }
        }

        private static void requireWithinRange(int value) {
            if (value < 1 || value > 6) {
                throw new IllegalArgumentException("Value was: " + value);
            }
        }
    }

}
