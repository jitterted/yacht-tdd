package com.jitterted.yacht.application;

import com.jitterted.yacht.application.port.AverageScoreFetcher;
import com.jitterted.yacht.application.port.ScoreCategoryNotifier;
import com.jitterted.yacht.domain.DiceRoll;
import com.jitterted.yacht.domain.Game;
import com.jitterted.yacht.domain.ScoreCategory;
import com.jitterted.yacht.domain.ScoredCategory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameService {
    private final ScoreCategoryNotifier scoreCategoryNotifier;
    private final DiceRoller diceRoller;
    private final AverageScoreFetcher averageScoreFetcher;

    private Game game;

    public GameService(DiceRoller diceRoller,
                       ScoreCategoryNotifier scoreCategoryNotifier,
                       AverageScoreFetcher averageScoreFetcher) {
        this.diceRoller = diceRoller;
        this.scoreCategoryNotifier = scoreCategoryNotifier;
        this.averageScoreFetcher = averageScoreFetcher;
    }

    public void start() {
        game = new Game();
    }

    public void rollDice() {
        game.rollDice(diceRoller.roll());
    }

    public DiceRoll lastRoll() {
        return game.lastRoll();
    }

    public boolean canReRoll() {
        return game.canReRoll();
    }

    public boolean roundCompleted() {
        return game.roundCompleted();
    }

    public void reRoll(List<Integer> keptDice) {
        game.reRoll(diceRoller.reRoll(keptDice));
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
