package com.jitterted.yacht.adapter.out.gamedatabase;

import com.jitterted.yacht.domain.Game;
import com.jitterted.yacht.domain.HandOfDice;
import com.jitterted.yacht.domain.ScoreCategory;
import com.jitterted.yacht.domain.Scoreboard;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import javax.persistence.EntityManager;
import javax.sql.DataSource;
import java.math.BigInteger;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;

@SuppressWarnings("unchecked")
@Tag("spring")
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class GameDatabaseTest {

    @Autowired
    DataSource dataSource;

    @Autowired
    EntityManager entityManager;

    @Autowired
    GameDatabaseJpa gameDatabaseJpa;

    @Test
    void writesCoreGameStateToDatabase() {
        saveGame(new SaveGameOptions()
                         .game(2, true, HandOfDice.of(3, 4, 4, 5, 6))
        );

        // from https://thorben-janssen.com/jpa-native-queries/
        List<Object[]> gameRows = executeQuery(
                "SELECT rolls, round_completed, current_hand FROM games");

        assertThat(gameRows)
                .containsExactly(new Object[]{
                        2,
                        true,
                        "3,4,4,5,6"
                });
    }

    @Test
    void writesScoreboardStateToDatabase() {
        saveGame(new SaveGameOptions()
                         .scoreboard(
                                 Map.of(ScoreCategory.FOURS, HandOfDice.of(1, 2, 4, 4, 4),
                                        ScoreCategory.SIXES, HandOfDice.of(6, 6, 5, 5, 4)
                                 )));

        List<BigInteger> gameRows = executeQuery("SELECT id FROM games");
        BigInteger gameId = gameRows.get(0);

        List<Object[]> scoreboardRows = executeQuery(
                "SELECT game_table_id, scoreboard_key, scoreboard FROM games_scoreboards");

        assertThat(scoreboardRows)
                .containsExactly(new Object[]{
                        gameId,
                        "FOURS",
                        "1,2,4,4,4"
                }, new Object[]{
                        gameId,
                        "SIXES",
                        "6,6,5,5,4"
                });
    }

    private List executeQuery(String sqlString) {
        return entityManager.createNativeQuery(sqlString).getResultList();
    }

    private void saveGame(SaveGameOptions saveGameOptions) {
        GameDatabase gameDatabase = new GameDatabase(gameDatabaseJpa);
        Game.Snapshot gameSnapshot = new Game.Snapshot(
                saveGameOptions.rolls,
                saveGameOptions.roundCompleted,
                saveGameOptions.currentHand,
                new Scoreboard.Snapshot(saveGameOptions.scoreboard));
        gameDatabase.saveGame(gameSnapshot);
    }

    private static class SaveGameOptions {
        int rolls = 42;
        boolean roundCompleted = false;
        HandOfDice currentHand = HandOfDice.of(1, 1, 1, 1, 1);
        Map<ScoreCategory, HandOfDice> scoreboard = Collections.emptyMap();

        SaveGameOptions game(int rolls, boolean roundCompleted, HandOfDice currentHand) {
            this.rolls = rolls;
            this.roundCompleted = roundCompleted;
            this.currentHand = currentHand;

            return this;
        }

        SaveGameOptions scoreboard(Map<ScoreCategory, HandOfDice> scoreboard) {
            this.scoreboard = scoreboard;

            return this;
        }
    }
}
