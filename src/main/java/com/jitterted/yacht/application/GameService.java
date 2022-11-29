package com.jitterted.yacht.application;

import com.jitterted.yacht.adapter.out.dieroller.DieRoller;
import com.jitterted.yacht.application.port.AverageScoreFetcher;
import com.jitterted.yacht.application.port.ScoreCategoryNotifier;
import com.jitterted.yacht.domain.DiceRoll;
import com.jitterted.yacht.domain.Game;
import com.jitterted.yacht.domain.ScoreCategory;
import com.jitterted.yacht.domain.ScoredCategory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameService {
    static final int YACHT_DICE_COUNT = 5;
    private final ScoreCategoryNotifier scoreCategoryNotifier;
    private final AverageScoreFetcher averageScoreFetcher;
    private final DieRoller dieRoller;

    private Game game;

    public GameService(ScoreCategoryNotifier scoreCategoryNotifier,
                       AverageScoreFetcher averageScoreFetcher,
                       DieRoller dieRoller) {
        this.scoreCategoryNotifier = scoreCategoryNotifier;
        this.averageScoreFetcher = averageScoreFetcher;
        this.dieRoller = dieRoller;
    }

    public void start() {
        game = new Game();
    }

    public void rollDice() {
        DiceRoll diceRoll = DiceRoll.from(dieRoller.rollMultiple(YACHT_DICE_COUNT));
        game.rollDice(diceRoll);
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
        List<Integer> dieRolls = new ArrayList<>();
        dieRolls.addAll(keptDice);
        dieRolls.addAll(dieRoller.rollMultiple(YACHT_DICE_COUNT - dieRolls.size()));
        game.reRoll(DiceRoll.from(dieRolls));
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
