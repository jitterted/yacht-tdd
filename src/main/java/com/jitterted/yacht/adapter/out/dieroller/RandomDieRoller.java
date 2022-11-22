package com.jitterted.yacht.adapter.out.dieroller;

import com.jitterted.yacht.application.port.DieRoller;

import java.util.Random;

public class RandomDieRoller implements DieRoller {

    private final RandomInt random;

    private RandomDieRoller(RandomInt randomInt) {
        this.random = randomInt;
    }

    public static RandomDieRoller create() {
        return new RandomDieRoller(new RandomWrapper());
    }

    static RandomDieRoller createNull() {
        return new RandomDieRoller(new RandomStub());
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
        @Override
        public int nextInt(int bound) {
            return 0;
        }
    }

}
