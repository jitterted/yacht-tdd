package com.jitterted.yacht.adapter.web;

import com.jitterted.yacht.application.DiceRoller;
import com.jitterted.yacht.application.GameService;
import com.jitterted.yacht.application.port.StubDieRoller;
import org.junit.jupiter.api.Test;
import org.springframework.ui.ConcurrentModel;
import org.springframework.ui.Model;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

public class YachtControllerInitialRollTest {

    @Test
    public void initialDiceRollReturnsRollInResultsPage() throws Exception {
        StubDieRoller dieRoller = new StubDieRoller(List.of(3, 1, 4, 1, 5));
        GameService gameService = new GameService(new DiceRoller(dieRoller));
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