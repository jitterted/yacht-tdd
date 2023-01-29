package com.jitterted.yacht.adapter.out.jpa;

import com.jitterted.yacht.domain.Game;
import com.jitterted.yacht.domain.HandOfDice;
import com.jitterted.yacht.domain.ScoreCategory;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.*;

@Tag("spring")
@DataJpaTest
public class GameRepositoryJpaAdapterTest {

    @Autowired
    GameJpaRepository gameJpaRepository;

    @Test
    void canTranslateAndSaveGameMementoToCrudRepository() {
        Game originalGame = createGameInProgress();
        GameRepositoryJpaAdapter gameRepositoryJpaAdapter =
                new GameRepositoryJpaAdapter(gameJpaRepository);

        Game savedGame = gameRepositoryJpaAdapter.save(originalGame);

        assertThat(savedGame)
                .usingRecursiveComparison() // note: does not use Game.equals()
                .isEqualTo(originalGame);

        Game foundGame = gameRepositoryJpaAdapter.find();

        assertThat(foundGame)
                .usingRecursiveComparison()
                .isEqualTo(originalGame);
    }

    @Test
    void canUpdateExistingMemento() {
        Game originalGame = createGameInProgress();
        GameRepositoryJpaAdapter gameRepositoryJpaAdapter =
                new GameRepositoryJpaAdapter(gameJpaRepository);

        gameRepositoryJpaAdapter.save(originalGame);

        Game modifiedGame = gameRepositoryJpaAdapter.find();

        modifiedGame.diceRolled(HandOfDice.of(2, 3, 4, 5, 6));
        modifiedGame.assignRollTo(ScoreCategory.BIGSTRAIGHT);

        gameRepositoryJpaAdapter.save(modifiedGame);

        Game foundGame = gameRepositoryJpaAdapter.find();

        assertThat(foundGame)
                .usingRecursiveComparison()
                .isEqualTo(modifiedGame);
    }

    private static Game createGameInProgress() {
        Game originalGame = new Game();
        originalGame.diceRolled(HandOfDice.of(3, 3, 4, 4, 5));
        originalGame.assignRollTo(ScoreCategory.FOURS);
        originalGame.diceRolled(HandOfDice.of(6, 6, 6, 6, 1));
        originalGame.diceReRolled(HandOfDice.of(6, 6, 6, 6, 6));
        return originalGame;
    }
}
