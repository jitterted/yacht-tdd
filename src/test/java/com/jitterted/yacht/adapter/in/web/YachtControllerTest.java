package com.jitterted.yacht.adapter.in.web;

import com.jitterted.yacht.adapter.OutputTracker;
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

@SuppressWarnings({"ConstantConditions"})
public class YachtControllerTest {

    private static final HandOfDice DUMMY_HAND = HandOfDice.of(1, 1, 1, 1, 1);
    private static final String DUMMY_SCORE_CATEGORY = ScoreCategory.THREES.toString();

    @Test
    public void startsGame() throws Exception {
        Fixture fixture = createFixture();

        String redirectPage = fixture.yachtController.startGame();

        assertThat(redirectPage)
                .isEqualTo("redirect:/game");
        assertThat(fixture.tracker.output())
                .containsExactly(new GameEvent.Started());
    }

    @Test
    public void canAssignCurrentHandToCategory() throws Exception {
        Game game = new Game();
        game.diceRolled(HandOfDice.of(1, 3, 3, 5, 5));
        Fixture fixture = createFixture(game);

        String redirectString = fixture.yachtController.assignCurrentHandToCategory(ScoreCategory.THREES.toString());

        assertThat(redirectString)
                .isEqualTo("redirect:/game");

        assertThat(fixture.tracker.output())
                .containsExactly(new GameEvent.CategoryAssigned(ScoreCategory.THREES));
    }

    @Test
    public void rollResultEndpointDisplaysGameOverPageWhenGameIsOver() throws Exception {
        Game game = createCompletedGame();
        Map<ScoreCategory, Double> averagesForScoreCategories =
                createAveragesForScoreCategories();
        Fixture fixture = createFixture(game, averagesForScoreCategories);

        var expectedMap = Map.of(
                "score", game.score(),
                "categories", ScoredCategoryView.viewOf(
                        game.scoredCategories(),
                        averagesForScoreCategories));

        Model model = new ConcurrentModel();
        String viewName = fixture.yachtController.game(model);

        assertThat(viewName)
                .isEqualTo("game-over");
        assertThat(model.asMap())
                .containsAllEntriesOf(expectedMap);

    }

    private static Game createCompletedGame() {
        Game game = new Game();
        for (ScoreCategory scoreCategory : ScoreCategory.values()) {
            game.diceRolled(DUMMY_HAND);
            game.assignCurrentHandTo(scoreCategory);
        }
        return game;
    }

    @Test
    public void rollResultEndpointDisplaysGameInProgressPageWhenGameIsNotOver() throws Exception {
        Map<ScoreCategory, Double> averageScores = createAveragesForScoreCategories();

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
        String page = fixture.yachtController.game(model);

        assertThat(page)
                .isEqualTo("roll-result");
        assertThat(model.asMap())
                .containsAllEntriesOf(expectedMap);
    }

    private static Map<ScoreCategory, Double> createAveragesForScoreCategories() {
        Map<ScoreCategory, Double> averageScores = new HashMap<>();
        for (ScoreCategory scoreCategory : ScoreCategory.values()) {
            averageScores.put(scoreCategory, 7.0);
        }
        return averageScores;
    }

    @Test
    public void canReroll() throws Exception {
        Game game = new Game();
        game.diceRolled(HandOfDice.of(6, 5, 4, 3, 2));
        Fixture fixture = createFixture(game);

        Keep keep = new Keep();
        keep.setDiceIndexesToKeep(List.of(0, 1, 3));

        String redirectPage = fixture.yachtController.reRoll(keep);

        assertThat(redirectPage)
               .isEqualTo("redirect:/game");
        assertThat(fixture.tracker.output())
                .containsExactly(new GameEvent.DiceRerolled(6, 5, 3));
    }

    private static Game createGameWithAllButOneCategoryAssigned(ScoreCategory scoreCategoryToSkip) {
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
