package com.jitterted.yacht.application;

import com.jitterted.yacht.adapter.out.averagescore.AverageScoreFetcher;
import com.jitterted.yacht.adapter.out.dieroller.DieRoller;
import com.jitterted.yacht.adapter.out.scorecategory.HttpScoreCategoryNotifier;
import com.jitterted.yacht.application.port.ScoreCategoryNotifier;
import com.jitterted.yacht.domain.Game;
import com.jitterted.yacht.domain.HandOfDice;
import com.jitterted.yacht.domain.ScoreCategory;
import com.jitterted.yacht.domain.ScoredCategory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameService {
    private static final int YACHT_DICE_COUNT = 5;
    private final ScoreCategoryNotifier scoreCategoryNotifier;
    private final AverageScoreFetcher averageScoreFetcher;
    private final DieRoller dieRoller;

    private Game game;

    GameService(ScoreCategoryNotifier scoreCategoryNotifier,
                       AverageScoreFetcher averageScoreFetcher,
                       DieRoller dieRoller) {
        this.scoreCategoryNotifier = scoreCategoryNotifier;
        this.averageScoreFetcher = averageScoreFetcher;
        this.dieRoller = dieRoller;
    }

    public static GameService create() {
        return new GameService(HttpScoreCategoryNotifier.create(),
                               AverageScoreFetcher.create(),
                               DieRoller.create());
    }

    public static GameService createNull() {
        return createNull(new NulledResponses());
    }

    public static GameService createNull(NulledResponses nulledResponses) {
        return new GameService(HttpScoreCategoryNotifier.createNull(),
                               AverageScoreFetcher.createNull(
                                       nulledResponses.averageScoreResponses),
                               DieRoller.createNull(
                                       nulledResponses.dieRolls
                               ));
    }

    public static class NulledResponses {
        private List<Integer> dieRolls = Collections.emptyList();
        private Map<ScoreCategory, Double> averageScoreResponses = Collections.emptyMap();

        public NulledResponses withDieRolls(List<Integer> rolls) {
            dieRolls = rolls;
            return this;
        }

        public NulledResponses withAverageScores(Map<ScoreCategory, Double> scoreResponses) {
            this.averageScoreResponses = scoreResponses;
            return this;
        }

        public NulledResponses withDieRolls(Integer... dieRolls) {
            return withDieRolls(Arrays.asList(dieRolls));
        }
    }


    public void start() {
        game = new Game();
    }

    public void rollDice() {
        HandOfDice handOfDice = HandOfDice.from(dieRoller.rollMultiple(YACHT_DICE_COUNT));
        game.diceRolled(handOfDice);
    }

    public HandOfDice lastRoll() {
        return game.lastRoll();
    }

    public boolean canReRoll() {
        return game.canReRoll();
    }

    public boolean roundCompleted() {
        return game.roundCompleted();
    }

    public void reRoll(List<Integer> keptDice) {
        List<Integer> dieRolls = new ArrayList<>();
        dieRolls.addAll(keptDice);
        dieRolls.addAll(dieRoller.rollMultiple(YACHT_DICE_COUNT - dieRolls.size()));
        game.diceReRolled(HandOfDice.from(dieRolls));
    }

    public void assignRollTo(ScoreCategory scoreCategory) {
        game.assignRollTo(scoreCategory);
        scoreCategoryNotifier.rollAssigned(game.lastRoll(),
                                           game.score(),
                                           scoreCategory);
    }

    public boolean isOver() {
        return game.isOver();
    }

    public List<ScoredCategory> scoredCategories() {
        return game.scoredCategories();
    }

    public int score() {
        return game.score();
    }

    public Map<ScoreCategory, Double> averagesFor(List<ScoreCategory> scoreCategories) {
        Map<ScoreCategory, Double> scoreToAverageMap = new HashMap<>();
        for (ScoreCategory scoreCategory : scoreCategories) {
            scoreToAverageMap.put(scoreCategory, averageScoreFetcher.averageFor(scoreCategory));
        }
        return scoreToAverageMap;
    }

}
