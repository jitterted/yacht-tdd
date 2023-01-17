package com.jitterted.yacht.adapter.out.scorecategory;

import com.jitterted.yacht.adapter.OutputListener;
import com.jitterted.yacht.adapter.OutputTracker;
import com.jitterted.yacht.adapter.out.JsonHttpClient;
import com.jitterted.yacht.application.port.ScoreCategoryNotifier;
import com.jitterted.yacht.domain.HandOfDice;
import com.jitterted.yacht.domain.ScoreCategory;

import java.net.URI;

public class HttpScoreCategoryNotifier implements ScoreCategoryNotifier {
    private static final URI YACHT_TRACKER_API_URI =
            URI.create("http://localhost:8080/api/scores");

    private final JsonHttpClient jsonHttpClient;

    private final OutputListener<RollAssignment> outputListener = new OutputListener<>();

    public static HttpScoreCategoryNotifier create() {
        return new HttpScoreCategoryNotifier(JsonHttpClient.create());
    }

    public static HttpScoreCategoryNotifier createNull() {
        return new HttpScoreCategoryNotifier(JsonHttpClient.createNull());
    }

    HttpScoreCategoryNotifier(JsonHttpClient jsonHttpClient) {
        this.jsonHttpClient = jsonHttpClient;
    }

    @Override
    public void rollAssigned(HandOfDice handOfDice,
                             int score,
                             ScoreCategory scoreCategory) {
        RollAssignedToCategory rollAssignedToCategory = RollAssignedToCategory
                .from(handOfDice, score, scoreCategory);

        outputListener.emit(new RollAssignment(handOfDice, score, scoreCategory));

        jsonHttpClient.post(YACHT_TRACKER_API_URI.toString(),
                            rollAssignedToCategory);
    }

    public OutputTracker<RollAssignment> trackAssignments() {
        return outputListener.createTracker();
    }

}
