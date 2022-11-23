package com.jitterted.yacht.adapter.out.dieroller;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class DieRollerTest {

    @Test
    void nullRollerReturnsOneWhenNotConfigured() throws Exception {
        DieRoller roller = DieRoller.createNull();

        assertThat(roller.roll())
                .isEqualTo(1);
    }

    @Test
    void nullRollerCanBeConfiguredToReturnSpecificValues() throws Exception {
        DieRoller roller = DieRoller.createNull(6, 3, 4, 3);

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