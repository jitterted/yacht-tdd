package com.jitterted.yacht.adapter.out.dieroller;

import com.jitterted.yacht.application.port.DieRoller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class RandomDieRoller implements DieRoller {

    private final RandomInt random;

    private RandomDieRoller(RandomInt randomInt) {
        this.random = randomInt;
    }

    public static RandomDieRoller create() {
        return new RandomDieRoller(new RandomWrapper());
    }

    static RandomDieRoller createNull(Integer... values) {
        return new RandomDieRoller(new RandomStub(values));
    }

    @Override
    public int roll() {
        return random.nextInt(6) + 1;
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
        private List<Integer> values;

        public RandomStub(Integer... values) {
            this.values = new ArrayList<>(Arrays.asList(values));
        }

        @Override
        public int nextInt(int bound) {
            if (values.size() == 0) {
                return 0;
            } else {
                int value = values.remove(0);
                requireWithinRange(value);
                return value - 1;
            }
        }

        private static void requireWithinRange(int value) {
            if (value < 1 || value > 6) {
                throw new IllegalArgumentException("Value was: " + value);
            }
        }
    }

}
