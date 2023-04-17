package com.jitterted.yacht.adapter.in.web;

import com.jitterted.yacht.adapter.OutputTracker;
import com.jitterted.yacht.adapter.out.gamedatabase.GameCorrupted;
import com.jitterted.yacht.application.GameService;
import com.jitterted.yacht.application.Keep;
import com.jitterted.yacht.domain.Game;
import com.jitterted.yacht.domain.GameEvent;
import com.jitterted.yacht.domain.HandOfDice;
import com.jitterted.yacht.domain.ScoreCategory;
import org.junit.jupiter.api.Test;
import org.springframework.ui.ConcurrentModel;
import org.springframework.ui.Model;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;

@SuppressWarnings({"unchecked", "ConstantConditions"})
public class YachtControllerAssignRollTest {

    private static final HandOfDice DUMMY_HAND = HandOfDice.of(1, 1, 1, 1, 1);
    private static final String DUMMY_SCORE_CATEGORY = ScoreCategory.THREES.toString();


    @Test
    public void canAssignCurrentHandToCategory() throws Exception {
        Game game = new Game();
        game.diceRolled(HandOfDice.of(1, 3, 3, 5, 5));
        Fixture fixture = createFixture(game);

        fixture.yachtController.assignCurrentHandToCategory(ScoreCategory.THREES.toString());

        assertThat(fixture.tracker.output())
                .containsExactly(new GameEvent.CategoryAssigned(ScoreCategory.THREES));
    }

    @Test
    public void assigningCategoryRedirectsToRollResult() throws Exception {
        Game game = new Game();
        game.diceRolled(DUMMY_HAND);
        Fixture fixture = createFixture(game);

        String redirectPage = fixture.yachtController.assignCurrentHandToCategory(
                DUMMY_SCORE_CATEGORY);

        assertThat(redirectPage)
                .isEqualTo("redirect:/rollresult");
    }

    @Test
    public void assigningLastCategoryRedirectsToGameOverPage() throws Exception {
        Game game = createGameWithAllButOneCategoryAssigned(ScoreCategory.TWOS);
        game.diceRolled(DUMMY_HAND);
        Fixture fixture = createFixture(game);

        String redirectPage = fixture.yachtController
                .assignCurrentHandToCategory(ScoreCategory.TWOS.toString());

        assertThat(redirectPage)
                .isEqualTo("redirect:/game-over");
    }

    @Test
    public void rollResultPagePopulatesModelWithGameInfo() throws Exception {
        Map<ScoreCategory, Double> averageScores = new HashMap<>();
        for (ScoreCategory scoreCategory : ScoreCategory.values()) {
            averageScores.put(scoreCategory, 7.0);
        }

        Game game = new Game();
        game.diceRolled(HandOfDice.of(4, 4, 4, 4, 4));
        game.assignCurrentHandTo(ScoreCategory.FOURS);
        Fixture fixture = createFixture(game, averageScores);

        var expectedMap = Map.of(
                "score", game.score(),
                "roll", RollView.listOf(game.currentHand()),
                "categories", ScoredCategoryView.viewOf(
                        game.scoredCategories(),
                        averageScores),
                "canReRoll", game.canReroll(),
                "roundCompleted", game.roundCompleted(),
                "keep", new Keep(),
                "categoryNames", ScoreCategory.values());

        Model model = new ConcurrentModel();
        fixture.yachtController.rollResult(model);

        assertThat(model.asMap())
                .containsAllEntriesOf(expectedMap);
    }

    @Test
    public void assignRollToAllCategoriesResultsInAllCategoriesAssignedWithAverages() throws Exception {
        GameService gameService = createGameServiceWithAllAverageScoresOf(12.0);
        YachtController yachtController = new YachtController(gameService);
        yachtController.startGame();
        rollAndAssignForAllCategories(gameService, yachtController);

        Model model = new ConcurrentModel();
        yachtController.rollResult(model);
        List<ScoredCategoryView> categories = (List<ScoredCategoryView>) model.getAttribute("categories");

        assertThat(categories.stream().allMatch(ScoredCategoryView::isRollAssigned))
                .isTrue();
        assertThat(categories)
                .extracting(ScoredCategoryView::getScoreAverage)
                .containsOnly("12.0");
    }

    @Test
    public void newGameAllCategoriesAreUnassigned() throws Exception {
        GameService gameService = GameService.createNull();
        YachtController yachtController = new YachtController(gameService);
        yachtController.startGame();

        Model model = new ConcurrentModel();
        yachtController.rollResult(model);

        List<ScoredCategoryView> categories = (List<ScoredCategoryView>) model.getAttribute("categories");

        assertThat(categories.stream().noneMatch(ScoredCategoryView::isRollAssigned))
                .isTrue();
    }

    private String rollAndAssignForAllCategories(GameService gameService, YachtController yachtController) throws GameCorrupted {
        String viewName = null;
        for (ScoreCategory scoreCategory : ScoreCategory.values()) {
            gameService.rollDice();
            viewName = yachtController.assignCurrentHandToCategory(scoreCategory.toString());
        }
        return viewName;
    }

    private Game createGameWithAllButOneCategoryAssigned(ScoreCategory scoreCategoryToSkip) {
        Game game = new Game();
        for (ScoreCategory scoreCategory : ScoreCategory.values()) {
            if (scoreCategory == scoreCategoryToSkip) {
                continue;
            }

            game.diceRolled(DUMMY_HAND);
            game.assignCurrentHandTo(scoreCategory);
        }
        return game;
    }

    private GameService createGameServiceWithAllAverageScoresOf(double averageScore) {
        Map<ScoreCategory, Double> map = new HashMap<>();
        for (ScoreCategory scoreCategory : ScoreCategory.values()) {
            map.put(scoreCategory, averageScore);
        }
        return GameService.createNull(
                new GameService.NulledResponses().withAverageScores(map));
    }


    private static GameService createGameServiceWithDieRollsOf(Integer... dieRolls) {
        return GameService.createNull(new GameService.NulledResponses()
                                              .withDieRolls(dieRolls));
    }

    record Fixture(YachtController yachtController,
                   GameService gameService,
                   OutputTracker<GameEvent> tracker) {
    }

    private static Fixture createFixture() {
        return createFixture(new Game());
    }

    private static Fixture createFixture(Game game) {
        return createFixture(game, Collections.emptyMap());
    }

    private static Fixture createFixture(Game game, Map<ScoreCategory, Double> averageScores) {
        GameService gameService = GameService.createNull(
                new GameService.NulledResponses()
                    .withGame(game)
                    .withAverageScores(averageScores));
        OutputTracker<GameEvent> tracker = gameService.trackEvents();
        YachtController yachtController = new YachtController(gameService);
        return new Fixture(yachtController, gameService, tracker);
    }

}
