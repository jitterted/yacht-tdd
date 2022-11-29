package com.jitterted.yacht.adapter.in.web;

import com.jitterted.yacht.adapter.out.dieroller.DieRoller;
import com.jitterted.yacht.application.DefaultAverageScoreFetcher;
import com.jitterted.yacht.application.DiceRoller;
import com.jitterted.yacht.application.GameService;
import org.junit.jupiter.api.Test;
import org.springframework.ui.ConcurrentModel;
import org.springframework.ui.Model;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

public class YachtControllerInitialRollTest {

    @Test
    public void initialDiceRollReturnsRollInResultsPage() throws Exception {
        DieRoller dieRoller = DieRoller.createNull(3, 1, 4, 1, 5);
        GameService gameService = new GameService(new DiceRoller(dieRoller), (diceRoll, score, scoreCategory) -> {
        }, new DefaultAverageScoreFetcher());
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