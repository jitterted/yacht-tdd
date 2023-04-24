package com.jitterted.yacht.adapter.out.gamedatabase;

import com.jitterted.yacht.adapter.OutputTracker;
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


    // ------ SAVE ------

    @Test
    void saveGameAlwaysWritesToSameIdBecauseWeOnlySupportOneGameForNow() {
        saveGame();

        List<BigInteger> ids = executeQuery(
                "SELECT id FROM games");

        assertThat(ids)
                .containsExactly(BigInteger.valueOf(THE_ONLY_GAME_ID));
    }

    @Test
    void saveGameWritesCoreGameStateToDatabase() {
        saveGame(new SaveGameOptions()
                         .rolls(2)
                         .roundCompleted(true)
                         .currentHand(HandOfDice.of(3, 4, 4, 5, 6))
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
    void saveGameCanWriteGamesThatHaveNoCurrentHand() {
        saveGame(new SaveGameOptions()
                         .currentHand(HandOfDice.unassigned()));

        List<Object[]> gameRows = executeQuery(
                "SELECT id, current_hand FROM games");

        assertThat(gameRows.get(0)[1])
                .isEqualTo("");
    }

    @Test
    void saveGameWritesScoreboardStateToDatabase() {
        saveGame(new SaveGameOptions()
                         .scoreboard(
                                 Map.of(ScoreCategory.FOURS, HandOfDice.of(1, 2, 4, 4, 4),
                                        ScoreCategory.SIXES, HandOfDice.of(6, 6, 5, 5, 4)
                                 )));

        List<BigInteger> gameRows = executeQuery("SELECT id FROM games");
        BigInteger gameId = gameRows.get(0);

        List<Object[]> scoreboardRows = executeQuery(
                "SELECT game_row_id, scoreboard_key, scoreboard FROM games_scoreboards");

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
    void saveGameUpdatesExistingGame() throws Exception {
        executeUpdate("INSERT INTO games " +
                              "(id, rolls, round_completed, current_hand) VALUES " +
                              "(" + THE_ONLY_GAME_ID + ", 3, true, '1,1,1,1,1')");

        saveGame(new SaveGameOptions()
                         .currentHand(HandOfDice.of(5, 5, 5, 5, 5)));

        var handRow = executeQuery("SELECT current_hand FROM games WHERE id=" + THE_ONLY_GAME_ID);
        assertThat(handRow)
                .containsExactly("5,5,5,5,5");
    }

    @Test
    void savingGamesIsTrackable() {
        GameDatabase gameDatabase = new GameDatabase(gameDatabaseJpa);

        OutputTracker<Game.Snapshot> outputTracker = gameDatabase.trackSaves();

        Game.Snapshot gameSnapshot = new Game.Snapshot(
                2,
                true,
                HandOfDice.of(1, 3, 5, 2, 4),
                new Scoreboard.Snapshot(Collections.emptyMap()));

        gameDatabase.saveGame(gameSnapshot);

        assertThat(outputTracker.output())
                .containsExactly(gameSnapshot);
    }


    // ----- LOAD -----

    @Test
    void loadsCoreGameState() throws Exception {
        executeUpdate("INSERT INTO games " +
                              "(id, rolls, round_completed, current_hand) VALUES " +
                              "(" + THE_ONLY_GAME_ID + ", 3, true, '1,2,3,4,4')");

        Game.Snapshot loadedGameSnapshot = loadGame().get();

        assertThat(loadedGameSnapshot)
                .isEqualTo(new Game.Snapshot(3,
                                             true,
                                             HandOfDice.of(1, 2, 3, 4, 4),
                                             new Scoreboard.Snapshot(Collections.emptyMap())
                ));
    }

    @Test
    void loadsGameWithNoCurrentHand() throws Exception {
        executeUpdate("INSERT INTO games " +
                              "(id, rolls, round_completed, current_hand) VALUES " +
                              "(" + THE_ONLY_GAME_ID + ", 42, false, '')");

        Game.Snapshot loadedGameSnapshot = loadGame().get();

        assertThat(loadedGameSnapshot.currentHand())
                .isEqualTo(HandOfDice.unassigned());
    }

    @Test
    void loadsScoreboardState() throws Exception {
        executeUpdate("INSERT INTO games " +
                              "(id, rolls, round_completed, current_hand) VALUES " +
                              "(" + THE_ONLY_GAME_ID + ", 3, true, '1,2,3,4,4')");
        executeUpdate("INSERT INTO games_scoreboards " +
                              "(game_row_id, scoreboard_key, scoreboard) VALUES " +
                              "(" + THE_ONLY_GAME_ID + ", 'FIVES', '5,5,5,5,5'), " +
                              "(" + THE_ONLY_GAME_ID + ", 'FOURS', '4,4,4,4,4')");

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


    // ----- EDGE CASES -----

    @Test
    void databaseConstraintPreventsDuplicateScoreboardRowsWithSameScoreCategory() {
        executeUpdate("INSERT INTO games " +
                              "(id, rolls, round_completed, current_hand) VALUES " +
                              "(" + THE_ONLY_GAME_ID + ", 3, true, '1,2,3,4,4')");

        assertThatThrownBy(() -> {
            executeUpdate("INSERT INTO games_scoreboards " +
                                  "(game_row_id, scoreboard_key, scoreboard) VALUES " +
                                  "(" + THE_ONLY_GAME_ID + ", 'FOURS', '5,4,4,5,5'), " +
                                  "(" + THE_ONLY_GAME_ID + ", 'FOURS', '4,4,4,4,4')");
        }).isInstanceOf(PersistenceException.class);
    }

    @Test
    void throwsExceptionWhenScoreCategoryDoesNotExist() {
        executeUpdate("INSERT INTO games " +
                              "(id, rolls, round_completed, current_hand) VALUES " +
                              "(" + THE_ONLY_GAME_ID + ", 3, true, '1,2,3,4,4')");
        executeUpdate("INSERT INTO games_scoreboards " +
                              "(game_row_id, scoreboard_key, scoreboard) VALUES " +
                              "(" + THE_ONLY_GAME_ID + ", 'NO_SUCH_CATEGORY', '5,4,4,5,5')");

        assertLoadGameThrowsGameCorrupted("Unrecognized ScoreCategory when loading game: NO_SUCH_CATEGORY");
    }

    @Test
    void throwsExceptionWhenCurrentHandHasNonIntegerDice() {
        executeUpdate("INSERT INTO games " +
                              "(id, rolls, round_completed, current_hand) VALUES " +
                              "(" + THE_ONLY_GAME_ID + ", 3, true, '6,6,6,1.5,6')");

        assertLoadGameThrowsGameCorrupted("Invalid hand of dice when loading game: [6,6,6,1.5,6]");
    }

    @Test
    void throwsExceptionWhenHandOfDiceIsInvalid() {
        executeUpdate("INSERT INTO games " +
                              "(id, rolls, round_completed, current_hand) VALUES " +
                              "(" + THE_ONLY_GAME_ID + ", 3, true, '6')");

        assertLoadGameThrowsGameCorrupted("Invalid hand of dice when loading game: [6]");
    }


    // ----- NULLABILITY ----

    @Test
    void nulledSaveGameDoesNotWriteToDatabase() {
        GameDatabase gameDatabase = GameDatabase.createNull();

        saveGame(new SaveGameOptions().gameDatabase(gameDatabase));

        List<BigInteger> ids = executeQuery("SELECT id FROM games");
        assertThat(ids)
                .isEmpty();
    }

    @Test
    void nulledLoadGameProvidesDefaultGame() throws Exception {
        GameDatabase gameDatabase = GameDatabase.createNull();
        Optional<Game.Snapshot> loadedGameSnapshot = gameDatabase.loadGame();

        Game.Snapshot defaultSnapshot = new Game.Snapshot(
                1,
                false,
                HandOfDice.of(1, 2, 3, 4, 5),
                new Scoreboard.Snapshot(Collections.emptyMap()));

        assertThat(loadedGameSnapshot)
                .contains(defaultSnapshot);
    }

    @Test
    void nulledLoadGameProvidesConfiguredGame() throws Exception {
        Game.Snapshot configuredSnapshot = new Game.Snapshot(
                2,
                true,
                HandOfDice.of(6, 6, 6, 6, 6),
                new Scoreboard.Snapshot(Collections.emptyMap()));

        GameDatabase gameDatabase = GameDatabase.createNull(configuredSnapshot);
        Optional<Game.Snapshot> loadedGameSnapshot = gameDatabase.loadGame();

        assertThat(loadedGameSnapshot)
                .contains(configuredSnapshot);
    }

    @Test
    void nulledLoadGameProvidesNoGame() throws Exception {
        GameDatabase gameDatabase = GameDatabase.createEmptyNull();
        Optional<Game.Snapshot> loadedGameSnapshot = gameDatabase.loadGame();

        assertThat(loadedGameSnapshot)
                .isEmpty();
    }

    @Test
    void loadGameThrowsExceptionWithCorruptedNull() {
        GameDatabase gameDatabase = GameDatabase.createCorruptedNull();

        assertThatThrownBy(gameDatabase::loadGame)
                .isExactlyInstanceOf(GameCorrupted.class);
    }


    // ----- HELPERS -----

    private Optional<Game.Snapshot> loadGame() throws Exception {
        GameDatabase gameDatabase = new GameDatabase(gameDatabaseJpa);
        Optional<Game.Snapshot> loadedGameSnapshot = gameDatabase.loadGame();
        return loadedGameSnapshot;
    }

    private void assertLoadGameThrowsGameCorrupted(String message) {
        assertThatThrownBy(this::loadGame)
                .isInstanceOf(GameCorrupted.class)
                .hasMessage(message);
    }

    private List executeQuery(String sqlString) {
        return entityManager.createNativeQuery(sqlString).getResultList();
    }

    private void executeUpdate(String sql) {
        entityManager.createNativeQuery(sql).executeUpdate();
    }

    private void saveGame() {
        saveGame(new SaveGameOptions());
    }

    private void saveGame(SaveGameOptions saveGameOptions) {
        Game.Snapshot gameSnapshot = new Game.Snapshot(
                saveGameOptions.rolls,
                saveGameOptions.roundCompleted,
                saveGameOptions.currentHand,
                new Scoreboard.Snapshot(saveGameOptions.scoreboard));
        saveGameOptions.gameDatabase.saveGame(gameSnapshot);
    }

    private class SaveGameOptions {
        GameDatabase gameDatabase = new GameDatabase(gameDatabaseJpa);
        int rolls = 42;
        boolean roundCompleted = false;
        HandOfDice currentHand = HandOfDice.of(1, 1, 1, 1, 1);
        Map<ScoreCategory, HandOfDice> scoreboard = Collections.emptyMap();

        SaveGameOptions rolls(int rolls) {
            this.rolls = rolls;
            return this;
        }

        SaveGameOptions roundCompleted(boolean roundCompleted) {
            this.roundCompleted = roundCompleted;
            return this;
        }

        SaveGameOptions currentHand(HandOfDice currentHand) {
            this.currentHand = currentHand;
            return this;
        }

        SaveGameOptions scoreboard(Map<ScoreCategory, HandOfDice> scoreboard) {
            this.scoreboard = scoreboard;

            return this;
        }

        public SaveGameOptions gameDatabase(GameDatabase gameDatabase) {
            this.gameDatabase = gameDatabase;

            return this;
        }
    }
}
