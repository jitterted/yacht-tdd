package com.jitterted.yacht.adapter.out.dieroller;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class RandomDieRollerTest {

    @Test
    void nullRollerReturnsOneWhenNotConfigured() throws Exception {
        RandomDieRoller roller = RandomDieRoller.createNull();

        assertThat(roller.roll())
                .isEqualTo(1);
    }

    @Test
    void nullRollerCanBeConfiguredToReturnSpecificValues() throws Exception {
        RandomDieRoller roller = RandomDieRoller.createNull(6, 3, 4, 3);

        assertThat(roller.roll())
                .isEqualTo(6);
        assertThat(roller.roll())
                .isEqualTo(3);
        assertThat(roller.roll())
                .isEqualTo(4);
        assertThat(roller.roll())
                .isEqualTo(3);
    }

}