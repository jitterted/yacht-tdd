package com.jitterted.yacht.domain;

import com.jitterted.yacht.adapter.out.dieroller.RandomDieRoller;
import com.jitterted.yacht.application.DiceRoller;
import com.jitterted.yacht.application.port.StubDieRoller;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

public class GameTest {

    @Test
    public void newGameResultsInScoreOfZero() throws Exception {
        Game game = new Game(new DiceRoller(new RandomDieRoller()));

        assertThat(game.score())
                .isZero();
    }

    @Test
    public void lastRollReturnsValueOfMostRecentRollDice() throws Exception {
        DiceRoller diceRoller = new DiceRoller(new StubDieRoller(List.of(6, 5, 4, 3, 2)));
        Game game = new Game(diceRoller);

        game.rollDice(diceRoller.roll());

        assertThat(game.lastRoll())
                .isEqualTo(DiceRoll.of(2, 3, 4, 5, 6));
    }

    @Test
    public void rollDiceThenQueryLastRollReturnsThatRoll() throws Exception {
        DiceRoller diceRoller = new DiceRoller(new RandomDieRoller());
        Game game = new Game(diceRoller);

        game.rollDice(diceRoller.roll());

        assertThat(game.lastRoll())
                .isNotNull();
    }

}
