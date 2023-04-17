package com.jitterted.yacht.application;

import com.jitterted.yacht.adapter.OutputListener;
import com.jitterted.yacht.adapter.OutputTracker;
import com.jitterted.yacht.adapter.out.averagescore.AverageScoreFetcher;
import com.jitterted.yacht.adapter.out.dieroller.DieRoller;
import com.jitterted.yacht.adapter.out.gamedatabase.GameCorrupted;
import com.jitterted.yacht.adapter.out.gamedatabase.GameDatabase;
import com.jitterted.yacht.adapter.out.scorecategory.ScoreCategoryNotifier;
import com.jitterted.yacht.domain.Game;
import com.jitterted.yacht.domain.GameEvent;
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
    private final GameDatabaseInterface gameDatabase;
    private final OutputListener<GameEvent> listener = new OutputListener<>();

    GameService(ScoreCategoryNotifier scoreCategoryNotifier,
                AverageScoreFetcher averageScoreFetcher,
                DieRoller dieRoller,
                GameDatabaseInterface gameDatabase) {
        this.scoreCategoryNotifier = scoreCategoryNotifier;
        this.averageScoreFetcher = averageScoreFetcher;
        this.dieRoller = dieRoller;
        this.gameDatabase = gameDatabase;
    }

    public static GameService create(GameDatabaseInterface gameDatabase) {
        return new GameService(ScoreCategoryNotifier.create(),
                               AverageScoreFetcher.create(),
                               DieRoller.create(),
                               gameDatabase);
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
                               nulledResponses.gameDatabase);
    }

    public OutputTracker<GameEvent> trackEvents() {
        return listener.createTracker();
    }

    public void start() {
        final Game game = new Game();
        gameDatabase.saveGame(game.snapshot());
        listener.emit(new GameEvent.Started());
    }

    public void rollDice() throws GameCorrupted {
        HandOfDice handOfDice = HandOfDice.from(dieRoller.rollMultiple(YACHT_DICE_COUNT));
        executeAndSave(game -> game.diceRolled(handOfDice));
        listener.emit(new GameEvent.DiceRolled());
    }

    public Game reRoll(List<Integer> keptDice) throws GameCorrupted {
        List<Integer> dieRolls = new ArrayList<>();
        dieRolls.addAll(keptDice);
        dieRolls.addAll(dieRoller.rollMultiple(YACHT_DICE_COUNT - dieRolls.size()));
        Game result = executeAndSave(game -> game.diceReRolled(HandOfDice.from(dieRolls)));
        listener.emit(new GameEvent.DiceRerolled(keptDice.toArray(new Integer[0])));
        return result;
    }

    public void assignCurrentHandTo(ScoreCategory scoreCategory) throws GameCorrupted {
        Game game = executeAndSave(g -> g.assignCurrentHandTo(scoreCategory));
        scoreCategoryNotifier.rollAssigned(game.currentHand(),
                                           game.score(),
                                           scoreCategory);
        listener.emit(new GameEvent.CategoryAssigned(scoreCategory));
    }

    private Game executeAndSave(Consumer<Game> consumer) throws GameCorrupted {
        Game game = loadGame();
        consumer.accept(game);
        gameDatabase.saveGame(game.snapshot());
        return game;
    }

    private Game loadGame() throws GameCorrupted {
        return Game.from(
                gameDatabase.loadGame()
                            .orElseThrow(
                                      () -> new IllegalStateException("Current design does not support that the Game might not be (or no longer be) in the database, but it SHOULD.")));
    }

    public HandOfDice currentHand() throws GameCorrupted {
        return loadGame().currentHand();
    }

    public boolean canReRoll() throws GameCorrupted {
        return loadGame().canReroll();
    }

    public boolean roundCompleted() throws GameCorrupted {
        return loadGame().roundCompleted();
    }

    public boolean isOver() throws GameCorrupted {
        return loadGame().isOver();
    }

    public List<ScoredCategory> scoredCategories() throws GameCorrupted {
        return loadGame().scoredCategories();
    }

    public int score() throws GameCorrupted {
        return loadGame().score();
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
//          private GameDatabaseInterface gameDatabase = GameDatabase.createNull();
       private GameDatabaseInterface gameDatabase = new DeleteMeImpl();

        public NulledResponses withDieRolls(List<Integer> rolls) {
            dieRolls = rolls;
            return this;
        }

        public NulledResponses withAverageScores(Map<ScoreCategory, Double> scoreResponses) {
            averageScoreResponses = scoreResponses;
            return this;
        }

        public NulledResponses withDieRolls(Integer... dieRolls) {
            return withDieRolls(Arrays.asList(dieRolls));
        }

        public NulledResponses withGame(Game game) {
//             gameDatabase = GameDatabase.createNull(game.snapshot());
            gameDatabase.saveGame(game.snapshot());
            return this;
        }

        public NulledResponses withCorruptedGame() {
            gameDatabase = GameDatabase.createCorruptedNull();
            return this;
        }
    }
}
