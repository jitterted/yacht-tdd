package com.jitterted.yacht.adapter.out.scorecategory;

import com.jitterted.yacht.application.port.ScoreCategoryNotifier;
import com.jitterted.yacht.domain.HandOfDice;
import com.jitterted.yacht.domain.ScoreCategory;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.stream.Collectors;

public class HttpScoreCategoryNotifier implements ScoreCategoryNotifier {
    private static final URI YACHT_TRACKER_API_URI =
            URI.create("http://localhost:8080/api/scores");

    private final RestTemplate restTemplate = new RestTemplate();


    @Override
    public void rollAssigned(HandOfDice handOfDice,
                             int score,
                             ScoreCategory scoreCategory) {
        RollAssignedToCategory rollAssignedToCategory =
                RollAssignedToCategory.from(handOfDice, score,
                                            scoreCategory);

        restTemplate.postForEntity(YACHT_TRACKER_API_URI,
                                   rollAssignedToCategory,
                                   Void.class);
    }

    private static class RollAssignedToCategory {
        private final String roll;
        private final String score;
        private final String category;

        public RollAssignedToCategory(String roll, String score, String category) {
            this.roll = roll;
            this.score = score;
            this.category = category;
        }

        private static RollAssignedToCategory from(HandOfDice handOfDice,
                                                   int score,
                                                   ScoreCategory scoreCategory) {
            String rollString = handOfDice.stream()
                                          .map(Object::toString)
                                          .collect(Collectors.joining(" "));
            String scoreString = String.valueOf(score);
            String categoryString = scoreCategory.toString();
            return new RollAssignedToCategory(rollString,
                                              scoreString,
                                              categoryString
            );
        }

        public String getRoll() {
            return roll;
        }

        public String getScore() {
            return score;
        }

        public String getCategory() {
            return category;
        }
    }
}
