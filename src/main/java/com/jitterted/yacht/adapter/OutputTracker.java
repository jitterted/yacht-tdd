package com.jitterted.yacht.adapter;

import java.util.ArrayList;
import java.util.List;

public class OutputTracker<T> {
    private final List<List<T>> listeners = new ArrayList<>();

    public void emit(T rollAssignment) {
        listeners.forEach(
                rollAssignments ->
                        rollAssignments.add(rollAssignment));
    }

    public List<T> createTracker() {
        List<T> rollAssignments = new ArrayList<>();
        listeners.add(rollAssignments);
        return rollAssignments;
    }
}
