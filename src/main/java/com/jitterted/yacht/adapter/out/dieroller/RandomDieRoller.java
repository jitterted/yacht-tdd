package com.jitterted.yacht.adapter.out.dieroller;

import com.jitterted.yacht.application.port.DieRoller;

import java.util.Random;

public class RandomDieRoller implements DieRoller {

    private final Random random = new Random();

    private RandomDieRoller() {
    }

    public static RandomDieRoller create() {
        return new RandomDieRoller();
    }

    static RandomDieRoller createNull() {
        return new RandomDieRoller();
    }

    @Override
    public int roll() {
        return random.nextInt(6) + 1;
    }
}
