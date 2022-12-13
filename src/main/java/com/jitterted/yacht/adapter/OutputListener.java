package com.jitterted.yacht.adapter;

import java.util.ArrayList;
import java.util.List;

public class OutputListener<T> {
    private final List<List<T>> listeners = new ArrayList<>();

    public void emit(T data) {
        listeners.forEach(
                rollAssignments ->
                        rollAssignments.add(data));
    }

    public List<T> createTracker() {
        List<T> tracker = new ArrayList<>();
        listeners.add(tracker);
        return tracker;
    }
}
