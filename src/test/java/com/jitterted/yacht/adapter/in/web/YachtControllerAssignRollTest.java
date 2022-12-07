package com.jitterted.yacht.adapter.in.web;

import com.jitterted.yacht.adapter.out.averagescore.AverageScoreFetcher;
import com.jitterted.yacht.adapter.out.dieroller.DieRoller;
import com.jitterted.yacht.application.GameService;
import com.jitterted.yacht.application.port.ScoreCategoryNotifier;
import com.jitterted.yacht.domain.ScoreCategory;
import org.junit.jupiter.api.Test;
import org.springframework.ui.ConcurrentModel;
import org.springframework.ui.Model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;

@SuppressWarnings({"unchecked", "ConstantConditions"})
public class YachtControllerAssignRollTest {

    private static final ScoreCategoryNotifier NO_OP_SCORE_CATEGORY_NOTIFIER = (diceRoll, score, scoreCategory) -> {
    };

    @Test
    public void assignDiceRoll13355ToThreesResultsInScoreOf6() throws Exception {
        GameService gameService = createGameServiceWithDieRollsOf(1, 3, 3, 5, 5);
        YachtController yachtController = new YachtController(gameService);
        yachtController.startGame();
        yachtController.rollDice();

        yachtController.assignRollToCategory(ScoreCategory.THREES.toString());

        Model model = new ConcurrentModel();
        yachtController.rollResult(model);
        assertThat(model.getAttribute("score"))
                .isEqualTo(String.valueOf(3 + 3));
    }

    @Test
    public void assignDiceRoll22244ToFullHouseResultsInScoreOf6() throws Exception {
        GameService gameService = createGameServiceWithDieRollsOf(4, 4, 2, 2, 2);
        YachtController yachtController = new YachtController(gameService);
        yachtController.startGame();
        yachtController.rollDice();

        yachtController.assignRollToCategory(ScoreCategory.FULLHOUSE.toString());

        assertThat(gameService.score())
                .isEqualTo(4 + 4 + 2 + 2 + 2);
    }

    @Test
    public void assignDiceRoll11123ToOnesResultsInScoreOf3() throws Exception {
        GameService gameService = createGameServiceWithDieRollsOf(1, 1, 1, 2, 3);
        YachtController yachtController = new YachtController(gameService);
        yachtController.startGame();
        yachtController.rollDice();

        yachtController.assignRollToCategory(ScoreCategory.ONES.toString());

        assertThat(gameService.score())
                .isEqualTo(1 + 1 + 1);
    }

    @Test
    public void assignToLastCategoryRedirectsToGameOverPage() throws Exception {
        GameService gameService = new GameService(NO_OP_SCORE_CATEGORY_NOTIFIER, AverageScoreFetcher.createNull(), DieRoller.create());
        YachtController yachtController = new YachtController(gameService);
        yachtController.startGame();

        String viewName = rollAndAssignForAllCategories(gameService, yachtController);

        assertThat(viewName)
                .isEqualTo("redirect:/game-over");
    }

    @Test
    public void assignRollToAllCategoriesResultsInAllCategoriesAssignedWithAverages() throws Exception {
        GameService gameService = new GameService(
                NO_OP_SCORE_CATEGORY_NOTIFIER,
                AverageScoreFetcher.createNull(mapOfAllCategoriesToAverageOf(12.0)),
                DieRoller.create());
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
        GameService gameService = new GameService(
                NO_OP_SCORE_CATEGORY_NOTIFIER,
                AverageScoreFetcher.createNull(), DieRoller.create());
        YachtController yachtController = new YachtController(gameService);
        yachtController.startGame();

        Model model = new ConcurrentModel();
        yachtController.rollResult(model);

        List<ScoredCategoryView> categories = (List<ScoredCategoryView>) model.getAttribute("categories");

        assertThat(categories.stream().noneMatch(ScoredCategoryView::isRollAssigned))
                .isTrue();
    }

    private String rollAndAssignForAllCategories(GameService gameService, YachtController yachtController) {
        String viewName = null;
        for (ScoreCategory scoreCategory : ScoreCategory.values()) {
            gameService.rollDice();
            viewName = yachtController.assignRollToCategory(scoreCategory.toString());
        }
        return viewName;
    }

    private Map<ScoreCategory, Double> mapOfAllCategoriesToAverageOf(double average) {
        Map<ScoreCategory, Double> map = new HashMap<>();
        for (ScoreCategory scoreCategory : ScoreCategory.values()) {
            map.put(scoreCategory, average);
        }
        return map;
    }


    private static GameService createGameServiceWithDieRollsOf(Integer... dies) {
        DieRoller dieRoller = DieRoller.createNull(dies);
        return new GameService(
                NO_OP_SCORE_CATEGORY_NOTIFIER,
                AverageScoreFetcher.createNull(),
                dieRoller);
    }

}
