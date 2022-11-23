package com.jitterted.yacht.adapter.web;

import com.jitterted.yacht.adapter.out.dieroller.RandomDieRoller;
import com.jitterted.yacht.application.DiceRoller;
import com.jitterted.yacht.application.GameService;
import com.jitterted.yacht.application.Keep;
import org.junit.jupiter.api.Test;
import org.springframework.ui.ConcurrentModel;
import org.springframework.ui.Model;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

public class YachtControllerReRollTest {

    @Test
    public void reRollGeneratesNewRollIncludingKeptDice() throws Exception {
        RandomDieRoller dieRoller = RandomDieRoller.createNull(3, 1, 4, 1, 5, 3, 2, 6);
        GameService gameService = new GameService(new DiceRoller(dieRoller));
        YachtController yachtController = new YachtController(gameService);
        yachtController.startGame();
        Keep keep = keep(List.of(1, 3, 4));

        yachtController.rollDice();
        yachtController.reRoll(keep);

        Model model = new ConcurrentModel();
        yachtController.rollResult(model);

        assertThat(model.getAttribute("roll"))
                .isEqualTo(List.of(1, 1, 5, 3, 2));
    }

    @Test
    public void afterThreeRollsThenCanReRollIsFalse() throws Exception {
        YachtController yachtController = new YachtController(new GameService(new DiceRoller(RandomDieRoller.createNull())));
        yachtController.startGame();
        Keep keep = keep(List.of(1, 3, 4));

        yachtController.rollDice();   // roll 1
        yachtController.reRoll(keep); // roll 2
        yachtController.reRoll(keep); // roll 3 (last allowed roll)

        Model model = new ConcurrentModel();
        yachtController.rollResult(model);

        assertThat(model.getAttribute("canReRoll"))
                .isEqualTo(Boolean.FALSE);
    }

    @Test
    public void afterTwoRollsThenCanReRollIsTrue() throws Exception {
        YachtController yachtController = new YachtController(new GameService(new DiceRoller(RandomDieRoller.createNull())));
        yachtController.startGame();
        Keep keep = keep(List.of(1, 3, 4));

        yachtController.rollDice();   // roll 1
        yachtController.reRoll(keep); // roll 2

        Model model = new ConcurrentModel();
        yachtController.rollResult(model);

        assertThat(model.getAttribute("canReRoll"))
                .isEqualTo(Boolean.TRUE);
    }

    private Keep keep(List<Integer> diceIndexesToKeep) {
        Keep keep = new Keep();
        keep.setDiceIndexesToKeep(diceIndexesToKeep);
        return keep;
    }

}