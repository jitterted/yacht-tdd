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

import static com.jitterted.yacht.adapter.out.gamedatabase.GameDatabase.THE_ONLY_GAME_ID;
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
    void alwaysWritesToSameIdBecauseWeOnlySupportOneGameForNow() {
        saveGame(new SaveGameOptions());

        List<Object[]> gameRows = executeQuery(
                "SELECT * FROM games WHERE id=" + THE_ONLY_GAME_ID);

        assertThat(gameRows)
                .hasSize(1);
    }

    @Test
    void writesCoreGameStateToDatabase() {
        saveGame(new SaveGameOptions()
                         .game(2, true, HandOfDice.of(3, 4, 4, 5, 6))
        );

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

    @Test
    void loadsCoreGameState() {
        GameDatabase gameDatabase = new GameDatabase(gameDatabaseJpa);
        String sqlString = "INSERT INTO games " +
                "(id, rolls, round_completed, current_hand) VALUES " +
                "(" + THE_ONLY_GAME_ID + ", 3, true, '1,2,3,4,4')";
        entityManager.createNativeQuery(sqlString).executeUpdate();

        Game.Snapshot loadedGameSnapshot = gameDatabase.loadGame();

        assertThat(loadedGameSnapshot)
                .isEqualTo(new Game.Snapshot(3,
                                             true,
                                             HandOfDice.of(1, 2, 3, 4, 4),
                                             new Scoreboard.Snapshot(Collections.emptyMap())
                                             ));
    }

    @Test
    void loadsScoreboardState() {
        GameDatabase gameDatabase = new GameDatabase(gameDatabaseJpa);
        String insertIntoGameSql = "INSERT INTO games " +
                "(id, rolls, round_completed, current_hand) VALUES " +
                "(" + THE_ONLY_GAME_ID + ", 3, true, '1,2,3,4,4')";
        entityManager.createNativeQuery(insertIntoGameSql).executeUpdate();
        String insertIntoScoreboardSql = "INSERT INTO games_scoreboards " +
                "(game_table_id, scoreboard_key, scoreboard) VALUES " +
                "(" + THE_ONLY_GAME_ID + ", 'FIVES', '5,5,5,5,5'), " +
                "(" + THE_ONLY_GAME_ID + ", 'FOURS', '4,4,4,4,4')";
        entityManager.createNativeQuery(insertIntoScoreboardSql).executeUpdate();

        Game.Snapshot loadedGameSnapshot = gameDatabase.loadGame();

        assertThat(loadedGameSnapshot.scoreboard())
                .isEqualTo(new Scoreboard.Snapshot(
                        Map.of(ScoreCategory.FIVES, HandOfDice.of(5, 5, 5, 5, 5),
                               ScoreCategory.FOURS, HandOfDice.of(4, 4, 4, 4, 4))));
    }

    // TODO Tests
    // "not found"
    // no game exists for ID
    // no scoreboard exists for game ID
    // "corruption"
    // duplicate scoreboard entries exist for game ID and score category

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
