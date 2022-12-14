package com.jitterted.yacht.adapter;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class OutputListenerTest {

    @Test
    void tracksOutputOverTime() {
        OutputListener<String> outputListener = new OutputListener<>();

        OutputTracker<String> tracker =
                outputListener.createTracker();

        assertThat(tracker.output())
                .isEmpty();

        outputListener.emit("one");

        assertThat(tracker.output())
                .containsExactly("one");

        outputListener.emit("two");

        assertThat(tracker.output())
                .containsExactly("one", "two");
    }

    @Test
    void clearsOutput() {
        OutputListener<String> outputListener = new OutputListener<>();

        OutputTracker<String> tracker =
                outputListener.createTracker();

        outputListener.emit("one");
        tracker.clear();
        outputListener.emit("two");

        assertThat(tracker.output())
                .containsExactly("two");
    }

    @Test
    void canStopTrackingOutput() {
        OutputListener<String> outputListener = new OutputListener<>();

        OutputTracker<String> tracker =
                outputListener.createTracker();

        outputListener.emit("one");

        tracker.stop();

        outputListener.emit("two");

        assertThat(tracker.output())
                .containsExactly("one");
    }

    @Test
    void trackersAreIndependent() {
        OutputListener<String> outputListener = new OutputListener<>();

        OutputTracker<String> tracker1 = outputListener.createTracker();

        outputListener.emit("one");

        OutputTracker<String> tracker2 = outputListener.createTracker();

        outputListener.emit("two");

        assertThat(tracker1.output())
                .containsExactly("one", "two");

        assertThat(tracker2.output())
                .containsExactly("two");
    }
}