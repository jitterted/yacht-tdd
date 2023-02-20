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
import javax.persistence.PersistenceException;
import javax.sql.DataSource;
import java.math.BigInteger;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.jitterted.yacht.adapter.out.gamedatabase.GameDatabase.THE_ONLY_GAME_ID;
import static org.assertj.core.api.Assertions.*;

@SuppressWarnings({"unchecked", "SqlResolve"})
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
    void loadsCoreGameState() throws Exception {
        String sqlString = "INSERT INTO games " +
                "(id, rolls, round_completed, current_hand) VALUES " +
                "(" + THE_ONLY_GAME_ID + ", 3, true, '1,2,3,4,4')";
        entityManager.createNativeQuery(sqlString).executeUpdate();

        Game.Snapshot loadedGameSnapshot = loadGame().get();

        assertThat(loadedGameSnapshot)
                .isEqualTo(new Game.Snapshot(3,
                                             true,
                                             HandOfDice.of(1, 2, 3, 4, 4),
                                             new Scoreboard.Snapshot(Collections.emptyMap())
                ));
    }

    @Test
    void loadsScoreboardState() throws Exception {
        String insertIntoGameSql = "INSERT INTO games " +
                "(id, rolls, round_completed, current_hand) VALUES " +
                "(" + THE_ONLY_GAME_ID + ", 3, true, '1,2,3,4,4')";
        entityManager.createNativeQuery(insertIntoGameSql).executeUpdate();
        String insertIntoScoreboardSql = "INSERT INTO games_scoreboards " +
                "(game_table_id, scoreboard_key, scoreboard) VALUES " +
                "(" + THE_ONLY_GAME_ID + ", 'FIVES', '5,5,5,5,5'), " +
                "(" + THE_ONLY_GAME_ID + ", 'FOURS', '4,4,4,4,4')";
        entityManager.createNativeQuery(insertIntoScoreboardSql).executeUpdate();

        Game.Snapshot loadedGameSnapshot = loadGame().get();

        assertThat(loadedGameSnapshot.scoreboard())
                .isEqualTo(new Scoreboard.Snapshot(
                        Map.of(ScoreCategory.FIVES, HandOfDice.of(5, 5, 5, 5, 5),
                               ScoreCategory.FOURS, HandOfDice.of(4, 4, 4, 4, 4))));
    }

    @Test
    void loadGameReturnsEmptyOptionalWhenGameRowDoesNotExist() throws Exception {
        Optional<Game.Snapshot> snapshot = loadGame();

        assertThat(snapshot)
                .isEmpty();
    }

    @Test
    void saveGameUpdatesExistingGame() throws Exception {
        String sqlString = "INSERT INTO games " +
                "(id, rolls, round_completed, current_hand) VALUES " +
                "(" + THE_ONLY_GAME_ID + ", 3, true, '1,1,1,1,1')";
        entityManager.createNativeQuery(sqlString).executeUpdate();

        saveGame(new SaveGameOptions()
                         .game(3, true, HandOfDice.of(5, 5, 5, 5, 5)));

        var gameRows = executeQuery("SELECT current_hand FROM games WHERE id=" + THE_ONLY_GAME_ID);
        assertThat(gameRows)
                .containsExactly("5,5,5,5,5");
    }

    //// Error handling cases:

    @Test
    void databaseConstraintPreventsDuplicateScoreboardRowsWithSameScoreCategory() {
        String insertIntoGameSql = "INSERT INTO games " +
                "(id, rolls, round_completed, current_hand) VALUES " +
                "(" + THE_ONLY_GAME_ID + ", 3, true, '1,2,3,4,4')";
        entityManager.createNativeQuery(insertIntoGameSql).executeUpdate();
        String insertIntoScoreboardSql = "INSERT INTO games_scoreboards " +
                "(game_table_id, scoreboard_key, scoreboard) VALUES " +
                "(" + THE_ONLY_GAME_ID + ", 'FOURS', '5,4,4,5,5'), " +
                "(" + THE_ONLY_GAME_ID + ", 'FOURS', '4,4,4,4,4')";

        assertThatThrownBy(() -> {
            entityManager.createNativeQuery(insertIntoScoreboardSql).executeUpdate();
        }).isInstanceOf(PersistenceException.class);
    }

    @Test
    void throwsExceptionWhenScoreCategoryDoesNotExist() {
        String insertIntoGameSql = "INSERT INTO games " +
                "(id, rolls, round_completed, current_hand) VALUES " +
                "(" + THE_ONLY_GAME_ID + ", 3, true, '1,2,3,4,4')";
        entityManager.createNativeQuery(insertIntoGameSql).executeUpdate();
        String insertIntoScoreboardSql = "INSERT INTO games_scoreboards " +
                "(game_table_id, scoreboard_key, scoreboard) VALUES " +
                "(" + THE_ONLY_GAME_ID + ", 'NO_SUCH_CATEGORY', '5,4,4,5,5')";
        entityManager.createNativeQuery(insertIntoScoreboardSql).executeUpdate();

        assertThatThrownBy(() -> {
            loadGame();
        }).isInstanceOf(GameCorrupted.class)
          .hasMessage("Unrecognized ScoreCategory when loading game: NO_SUCH_CATEGORY");
    }

    @Test
    void throwsExceptionWhenCurrentHandHasWrongNumberOfDice() {
        String insertIntoGameSql = "INSERT INTO games " +
                "(id, rolls, round_completed, current_hand) VALUES " +
                "(" + THE_ONLY_GAME_ID + ", 3, true, '6,6,6')";
        entityManager.createNativeQuery(insertIntoGameSql).executeUpdate();

        assertThatThrownBy(() -> {
            loadGame();
        }).isInstanceOf(GameCorrupted.class)
          .hasMessage("Invalid hand of dice when loading game: 6,6,6");
    }

    @Test
    void throwsExceptionWhenCurrentHandHasNonIntegerDice() {
        String insertIntoGameSql = "INSERT INTO games " +
                "(id, rolls, round_completed, current_hand) VALUES " +
                "(" + THE_ONLY_GAME_ID + ", 3, true, '6,6,6,1.5,6')";
        entityManager.createNativeQuery(insertIntoGameSql).executeUpdate();

        assertThatThrownBy(() -> {
            loadGame();
        }).isInstanceOf(GameCorrupted.class)
          .hasMessage("Invalid hand of dice when loading game: 6,6,6,1.5,6");
    }

    @Test
    void throwsExceptionWhenCurrentHandHasDiceThatAreTooLarge() {
        String insertIntoGameSql = "INSERT INTO games " +
                "(id, rolls, round_completed, current_hand) VALUES " +
                "(" + THE_ONLY_GAME_ID + ", 3, true, '6,5,6,7,6')";
        entityManager.createNativeQuery(insertIntoGameSql).executeUpdate();

        assertThatThrownBy(() -> {
            loadGame();
        }).isInstanceOf(GameCorrupted.class)
          .hasMessage("Invalid hand of dice when loading game: 6,5,6,7,6");
    }

    @Test
    void throwsExceptionWhenCurrentHandHasDiceThatAreTooSmall() {
        String insertIntoGameSql = "INSERT INTO games " +
                "(id, rolls, round_completed, current_hand) VALUES " +
                "(" + THE_ONLY_GAME_ID + ", 3, true, '1,2,1,0,1')";
        entityManager.createNativeQuery(insertIntoGameSql).executeUpdate();

        assertThatThrownBy(() -> {
            loadGame();
        }).isInstanceOf(GameCorrupted.class)
          .hasMessage("Invalid hand of dice when loading game: 1,2,1,0,1");
    }

    private Optional<Game.Snapshot> loadGame() throws Exception {
        GameDatabase gameDatabase = new GameDatabase(gameDatabaseJpa);
        Optional<Game.Snapshot> loadedGameSnapshot = gameDatabase.loadGame();
        return loadedGameSnapshot;
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
