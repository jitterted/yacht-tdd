package com.jitterted.yacht.adapter.vue;

import com.jitterted.yacht.adapter.out.dieroller.DieRoller;
import com.jitterted.yacht.application.DiceRoller;
import com.jitterted.yacht.application.GameService;
import com.jitterted.yacht.application.Keep;
import com.jitterted.yacht.domain.ScoreCategory;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

public class VueControllerTest {

    @Test
    public void callingStateOnNonStartedGameThrowsException() throws Exception {
        GameService gameService = new GameService(new DiceRoller(DieRoller.createNull()));

        VueController vueController = new VueController(gameService);

        assertThatThrownBy(() -> {
            gameService.roundCompleted();
        }).isInstanceOf(NullPointerException.class);
    }

    @Test
    public void postToStartGameStartsGame() throws Exception {
        GameService gameService = new GameService(new DiceRoller(DieRoller.createNull()));
        VueController vueController = new VueController(gameService);

        vueController.startGame();

        assertThat(gameService.roundCompleted())
                .isTrue();
    }

    @Test
    public void newGameStartedWhenGetLastRollReturnsEmptyDiceRoll() throws Exception {
        GameService gameService = new GameService(new DiceRoller(DieRoller.createNull()));
        VueController vueController = new VueController(gameService);
        vueController.startGame();

        DiceRollDto dto = vueController.lastRoll();

        assertThat(dto.getRoll())
                .isEmpty();
    }

    @Test
    public void gameStartedRollDiceButtonRollsTheDice() throws Exception {
        DieRoller dieRoller = DieRoller.createNull(2, 3, 4, 5, 6);
        GameService gameService = new GameService(new DiceRoller(dieRoller));
        VueController vueController = new VueController(gameService);

        vueController.startGame();

        vueController.rollDice();
        DiceRollDto dto = vueController.lastRoll();

        assertThat(dto.getRoll())
                .isEqualTo(List.of(2, 3, 4, 5, 6));
    }

    @Test
    public void scoreCategoriesReturnsScoredCategories() throws Exception {
        GameService gameService = new GameService(new DiceRoller(DieRoller.createNull()));
        VueController vueController = new VueController(gameService);
        vueController.startGame();

        ScoreCategoriesDto scoreCategoriesDto = vueController.scoringCategories();

        assertThat(scoreCategoriesDto.getTotalScore())
                .isZero();
        assertThat(scoreCategoriesDto.getCategories())
                .hasSize(ScoreCategory.values().length);
    }

    @Test
    public void assignLastRollToCategoryThenCategoryIsAssignedAndScored() throws Exception {
        DieRoller dieRoller = DieRoller.createNull(6, 6, 5, 5, 5);
        GameService gameService = new GameService(new DiceRoller(dieRoller));
        VueController vueController = new VueController(gameService);
        vueController.startGame();
        vueController.rollDice();

        vueController.assignRollToCategory(Map.of("category", "SIXES"));

        ScoreCategoriesDto scoreCategoriesDto = vueController.scoringCategories();
        assertThat(scoreCategoriesDto.getTotalScore())
                .isEqualTo(6 + 6);
    }

    @Test
    @Disabled // fix this by creating a hand-rolled Mock/Spy
    public void keepDiceReRollsTheNonKeptDiceUsingSpy() throws Exception {
        GameService spyGameService = spy(GameService.class);
        VueController vueController = new VueController(spyGameService);
        vueController.startGame();
        vueController.rollDice();

        Keep keep = new Keep();
        keep.setDiceIndexesToKeep(List.of(1, 2, 4));
        List<Integer> keptDiceValues = keep.diceValuesFrom(spyGameService.lastRoll());

        vueController.reroll(keep);

        verify(spyGameService).reRoll(keptDiceValues);
    }

}