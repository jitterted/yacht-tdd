package com.jitterted.yacht.adapter.out.dieroller;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class RandomDieRollerTest {

    @Test
    void nullRollerAlwaysReturnsOne() throws Exception {
        RandomDieRoller roller = RandomDieRoller.createNull();

        assertThat(roller.roll())
                .isEqualTo(1);
    }

}