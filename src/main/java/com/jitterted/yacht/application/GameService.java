package com.jitterted.yacht.application;

import com.jitterted.yacht.adapter.out.averagescore.AverageScoreFetcher;
import com.jitterted.yacht.adapter.out.dieroller.DieRoller;
import com.jitterted.yacht.adapter.out.scorecategory.ScoreCategoryNotifier;
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
import java.util.function.Consumer;

public class GameService {
    private static final int YACHT_DICE_COUNT = 5;
    private final ScoreCategoryNotifier scoreCategoryNotifier;
    private final AverageScoreFetcher averageScoreFetcher;
    private final DieRoller dieRoller;
    private final GameRepository gameRepository;

    GameService(ScoreCategoryNotifier scoreCategoryNotifier,
                AverageScoreFetcher averageScoreFetcher,
                DieRoller dieRoller,
                GameRepository gameRepository) {
        this.scoreCategoryNotifier = scoreCategoryNotifier;
        this.averageScoreFetcher = averageScoreFetcher;
        this.dieRoller = dieRoller;
        this.gameRepository = gameRepository;
    }

    public static GameService create() {
        return new GameService(ScoreCategoryNotifier.create(),
                               AverageScoreFetcher.create(),
                               DieRoller.create(),
                               new InMemoryGameRepository());
    }

    public static GameService createNull() {
        return createNull(new NulledResponses());
    }

    public static GameService createNull(NulledResponses nulledResponses) {
        return new GameService(ScoreCategoryNotifier.createNull(),
                               AverageScoreFetcher.createNull(
                                       nulledResponses.averageScoreResponses),
                               DieRoller.createNull(
                                       nulledResponses.dieRolls
                               ),
                               new InMemoryGameRepository());
    }



    public void start() {
        gameRepository.save(new Game());
    }

    public void rollDice() {
        HandOfDice handOfDice = HandOfDice.from(dieRoller.rollMultiple(YACHT_DICE_COUNT));
        executeAndSave(game -> game.diceRolled(handOfDice));
    }

    public void reRoll(List<Integer> keptDice) {
        List<Integer> dieRolls = new ArrayList<>();
        dieRolls.addAll(keptDice);
        dieRolls.addAll(dieRoller.rollMultiple(YACHT_DICE_COUNT - dieRolls.size()));
        executeAndSave(game -> game.diceReRolled(HandOfDice.from(dieRolls)));
    }

    private void executeAndSave(Consumer<Game> consumer) {
        Game game = gameRepository.find();
        consumer.accept(game);
        gameRepository.save(game);
    }

    public void assignRollTo(ScoreCategory scoreCategory) {
        executeAndSave(game -> game.assignRollTo(scoreCategory));
        Game game = gameRepository.find();
        scoreCategoryNotifier.rollAssigned(game.lastRoll(),
                                           game.score(),
                                           scoreCategory);
    }

    public HandOfDice lastRoll() {
        return gameRepository.find().lastRoll();
    }

    public boolean canReRoll() {
        return gameRepository.find().canReRoll();
    }

    public boolean roundCompleted() {
        return gameRepository.find().roundCompleted();
    }

    public boolean isOver() {
        return gameRepository.find().isOver();
    }

    public List<ScoredCategory> scoredCategories() {
        return gameRepository.find().scoredCategories();
    }

    public int score() {
        return gameRepository.find().score();
    }

    public Map<ScoreCategory, Double> averagesFor(List<ScoreCategory> scoreCategories) {
        Map<ScoreCategory, Double> scoreToAverageMap = new HashMap<>();
        for (ScoreCategory scoreCategory : scoreCategories) {
            scoreToAverageMap.put(scoreCategory, averageScoreFetcher.averageFor(scoreCategory));
        }
        return scoreToAverageMap;
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
}
