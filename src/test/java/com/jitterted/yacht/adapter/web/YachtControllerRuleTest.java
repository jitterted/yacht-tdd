package com.jitterted.yacht.adapter.web;

import com.jitterted.yacht.application.GameService;
import com.jitterted.yacht.domain.Game;
import com.jitterted.yacht.domain.ScoreCategory;
import org.junit.jupiter.api.Test;
import org.springframework.ui.ConcurrentModel;
import org.springframework.ui.Model;

import static org.assertj.core.api.Assertions.*;

public class YachtControllerRuleTest {

    @Test
    public void newGameDoesNotRollDiceSoNoRollToAssign() throws Exception {
        Game game = new Game();
        YachtController yachtController = new YachtController(new GameService(game));

        yachtController.startGame();

        assertThat(game.roundCompleted())
                .isTrue();
    }

    @Test
    public void givenRollHasNotBeenAssignedThenRollAssignedToCategoryIsFalse() throws Exception {
        Game game = new Game();
        YachtController yachtController = new YachtController(new GameService(game));
        yachtController.rollDice();

        Model model = new ConcurrentModel();
        yachtController.rollResult(model);

        assertThat(model.getAttribute("roundCompleted"))
                .isEqualTo(Boolean.FALSE);
    }

    @Test
    public void givenRollWhenAssignedThenRollAssignedToCategoryIsTrue() throws Exception {
        Game game = new Game();
        YachtController yachtController = new YachtController(new GameService(game));
        yachtController.rollDice();

        yachtController.assignRollToCategory(ScoreCategory.FULLHOUSE.toString());

        Model model = new ConcurrentModel();
        yachtController.rollResult(model);

        assertThat(model.getAttribute("roundCompleted"))
                .isEqualTo(Boolean.TRUE);
    }
}
