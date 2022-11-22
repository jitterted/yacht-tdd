package com.jitterted.yacht.adapter.web;

import com.jitterted.yacht.StubDiceRoller;
import com.jitterted.yacht.application.GameService;
import org.junit.jupiter.api.Test;
import org.springframework.ui.ConcurrentModel;
import org.springframework.ui.Model;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

public class YachtControllerInitialRollTest {

    @Test
    public void initialDiceRollReturnsRollInResultsPage() throws Exception {
        GameService gameService = new GameService(StubDiceRoller.createDiceRollerFor(3, 1, 4, 1, 5));
        YachtController yachtController = new YachtController(gameService);
        yachtController.startGame();

        yachtController.rollDice();
        Model model = new ConcurrentModel();
        yachtController.rollResult(model);

        assertThat(model.getAttribute("roll"))
                .isEqualTo(List.of(3, 1, 4, 1, 5));

        assertThat(model.getAttribute("score"))
                .isEqualTo("0");
    }

}