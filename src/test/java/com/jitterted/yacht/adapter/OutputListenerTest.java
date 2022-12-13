package com.jitterted.yacht.adapter;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

class OutputListenerTest {

    @Test
    void tracksOutputOverTime() {
        OutputListener<String> outputListener = new OutputListener<>();

        List<String> tracker =
                outputListener.createTracker();

        assertThat(tracker)
                .isEmpty();

        outputListener.emit("one");

        assertThat(tracker)
                .containsExactly("one");

        outputListener.emit("two");

        assertThat(tracker)
                .containsExactly("one", "two");
    }


    @Test
    void trackersAreIndependent() {
        OutputListener<String> outputListener = new OutputListener<>();

        List<String> tracker1 = outputListener.createTracker();

        outputListener.emit("one");

        List<String> tracker2 = outputListener.createTracker();

        outputListener.emit("two");

        assertThat(tracker1)
                .containsExactly("one", "two");

        assertThat(tracker2)
                .containsExactly("two");
    }
}