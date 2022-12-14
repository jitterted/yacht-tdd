package com.jitterted.yacht.adapter;

import java.util.ArrayList;
import java.util.List;

public class OutputTracker<T> {
    private final List<T> output = new ArrayList<>();
    private final OutputListener<T> outputListener;

    public OutputTracker(OutputListener<T> outputListener) {
        this.outputListener = outputListener;
    }

    void add(T data) {
        output.add(data);
    }

    public List<T> output() {
        return List.copyOf(output);
    }

    public void clear() {
        output.clear();
    }

    public void stop() {
        outputListener.remove(this);
    }
}
