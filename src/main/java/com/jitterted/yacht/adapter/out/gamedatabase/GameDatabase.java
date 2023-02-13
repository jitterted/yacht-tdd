package com.jitterted.yacht.adapter.out.gamedatabase;

import com.jitterted.yacht.domain.Game;
import com.jitterted.yacht.domain.HandOfDice;
import com.jitterted.yacht.domain.ScoreCategory;
import com.jitterted.yacht.domain.Scoreboard;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class GameDatabase {

    static final Long THE_ONLY_GAME_ID = 777L;
    private final GameDatabaseJpa gameDatabaseJpa;

    @Autowired
    public GameDatabase(GameDatabaseJpa gameDatabaseJpa) {
        this.gameDatabaseJpa = gameDatabaseJpa;
    }

    public void saveGame(Game.Snapshot snapshot) {
        GameTable gameTable = new GameTable();
        gameTable.setId(THE_ONLY_GAME_ID);

        gameTable.setRolls(snapshot.rolls());
        gameTable.setRoundCompleted(snapshot.roundCompleted());
        gameTable.setCurrentHand(asPersistableHand(snapshot.currentHand()));
        gameTable.setScoreboard(asPersistableScoreboard(snapshot.scoreboard()));

        gameDatabaseJpa.save(gameTable);
    }

    private static Map<String, String> asPersistableScoreboard(Scoreboard.Snapshot scoreboard) {
        return scoreboard.scoredCategoryHandMap()
                         .entrySet()
                         .stream()
                         .collect(Collectors.toMap(
                                 entry -> entry.getKey().toString(),
                                 entry -> asPersistableHand(entry.getValue())));
    }

    public Optional<Game.Snapshot> loadGame() throws GameCorrupted {
        try {
            return gameDatabaseJpa
                    .findById(THE_ONLY_GAME_ID)
                    .map(this::fromPersistedGame);
        } catch (GameCorruptedInternalException e) {
            throw new GameCorrupted(e.getMessage());
        }
    }

    private Game.Snapshot fromPersistedGame(GameTable gameTable) {
        return new Game.Snapshot(
                gameTable.getRolls(),
                gameTable.isRoundCompleted(),
                fromPersistedHand(gameTable.getCurrentHand()),
                fromPersistedScoreboard(gameTable.getScoreboard()));
    }

    private Scoreboard.Snapshot fromPersistedScoreboard(Map<String, String> scoreboardStrings) {
        Map<ScoreCategory, HandOfDice> map = scoreboardStrings
                .entrySet()
                .stream()
                .collect(Collectors.toMap(
                        entry -> fromPersistedScoreCategory(entry),
                        entry -> fromPersistedHand(entry.getValue()))
                );
        return new Scoreboard.Snapshot(map);
    }

    private ScoreCategory fromPersistedScoreCategory(Map.Entry<String, String> entry) {
        try {
            return ScoreCategory.valueOf(entry.getKey());
        } catch (IllegalArgumentException e) {
            throw new GameCorruptedInternalException(
                    "Unrecognized ScoreCategory when loading game: " +
                            entry.getKey());
        }
    }

    private static String asPersistableHand(HandOfDice handOfDice) {
        return handOfDice
                .stream()
                .map(String::valueOf)
                .collect(Collectors.joining(","));
    }

    private HandOfDice fromPersistedHand(String handOfDice) {
        List<Integer> integers = null;
        try {
            integers = Arrays.stream(handOfDice.split(","))
                             .map(die -> dieValue(die, handOfDice))
                             .toList();
        } catch (NumberFormatException nfe) {
            throw new GameCorruptedInternalException(
                    "Invalid hand of dice when loading game: "
                            + handOfDice
            );
        }
        if (integers.size() != 5) {
            throw new GameCorruptedInternalException(
                    "Invalid hand of dice when loading game: "
                            + handOfDice);
        }
        return HandOfDice.from(integers);
    }

    private Integer dieValue(String die, String handOfDice) {
        int dieValue = Integer.parseInt(die);
        if (dieValue < 1 || dieValue > 6) {
            throw new GameCorruptedInternalException(
                    "Invalid hand of dice when loading game: "
            + handOfDice);
        }
        return dieValue;
    }

    private static class GameCorruptedInternalException extends RuntimeException {
        public GameCorruptedInternalException(String message) {
            super(message);
        }
    }
}
