package com.jitterted.yacht.adapter.in.web;

import com.jitterted.yacht.application.GameService;
import com.jitterted.yacht.domain.ScoreCategory;
import org.junit.jupiter.api.Test;
import org.springframework.ui.ConcurrentModel;
import org.springframework.ui.Model;

import static org.assertj.core.api.Assertions.*;

public class YachtControllerRuleTest {

    @Test
    public void newGameDoesNotRollDiceSoNoRollToAssign() throws Exception {
        GameService gameService = GameService.createNull();
        YachtController yachtController = new YachtController(gameService);

        yachtController.startGame();

        assertThat(gameService.roundCompleted())
                .isTrue();
    }

    @Test
    public void givenRollHasNotBeenAssignedThenRollAssignedToCategoryIsFalse() throws Exception {
        YachtController yachtController = new YachtController(GameService.createNull());
        yachtController.startGame();
        yachtController.rollDice();

        Model model = new ConcurrentModel();
        yachtController.rollResult(model);

        assertThat(model.getAttribute("roundCompleted"))
                .isEqualTo(Boolean.FALSE);
    }

    @Test
    public void givenRollWhenAssignedThenRollAssignedToCategoryIsTrue() throws Exception {
        YachtController yachtController = new YachtController(GameService.createNull());
        yachtController.startGame();
        yachtController.rollDice();

        yachtController.assignCurrentHandToCategory(ScoreCategory.FULLHOUSE.toString());

        Model model = new ConcurrentModel();
        yachtController.rollResult(model);

        assertThat(model.getAttribute("roundCompleted"))
                .isEqualTo(Boolean.TRUE);
    }
}
