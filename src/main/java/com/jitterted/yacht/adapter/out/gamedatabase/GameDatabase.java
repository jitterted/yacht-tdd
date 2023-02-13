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

    public Game.Snapshot loadGame() {
        GameTable gameTable = gameDatabaseJpa.findById(THE_ONLY_GAME_ID)
                                             .orElseThrow();
        return new Game.Snapshot(
                gameTable.getRolls(),
                gameTable.isRoundCompleted(),
                fromPersistedHand(gameTable.getCurrentHand()),
                fromPersistedScoreboard(gameTable.getScoreboard()));
    }

    private Scoreboard.Snapshot fromPersistedScoreboard(Map<String, String> scoreboardStrings) {
        Map<ScoreCategory, HandOfDice> map =
                scoreboardStrings
                        .entrySet()
                        .stream()
                        .collect(Collectors.toMap(
                                entry -> ScoreCategory.valueOf(entry.getKey()),
                                entry -> fromPersistedHand(entry.getValue()))
                        );
        return new Scoreboard.Snapshot(map);
    }

    private static String asPersistableHand(HandOfDice handOfDice) {
        return handOfDice
                .stream()
                .map(String::valueOf)
                .collect(Collectors.joining(","));
    }

    private HandOfDice fromPersistedHand(String handOfDice) {
        List<Integer> integers = Arrays.stream(handOfDice.split(","))
                                       .map(Integer::valueOf)
                                       .toList();
        return HandOfDice.from(integers);
    }
}
