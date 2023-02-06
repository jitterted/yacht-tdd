package com.jitterted.yacht.adapter.out.gamedatabase;

import com.jitterted.yacht.domain.Game;
import com.jitterted.yacht.domain.HandOfDice;
import com.jitterted.yacht.domain.Scoreboard;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.stream.Collectors;

@Repository
public class GameDatabase {

    private final GameDatabaseJpa gameDatabaseJpa;

    @Autowired
    public GameDatabase(GameDatabaseJpa gameDatabaseJpa) {
        this.gameDatabaseJpa = gameDatabaseJpa;
    }

    public void saveGame(Game.Snapshot snapshot) {
        GameTable gameTable = new GameTable();

        gameTable.setRolls(snapshot.rolls());
        gameTable.setRoundCompleted(snapshot.roundCompleted());
        gameTable.setCurrentHand(asPersistable(snapshot.currentHand()));
        gameTable.setScoreboard(asPersistable(snapshot.scoreboard()));

        gameDatabaseJpa.save(gameTable);
    }

    private static Map<String, String> asPersistable(Scoreboard.Snapshot scoreboard) {
        return scoreboard.scoredCategoryHandMap()
                         .entrySet()
                         .stream()
                         .collect(Collectors.toMap(
                                 entry -> entry.getKey().toString(),
                                 entry -> asPersistable(entry.getValue())));
    }

    private static String asPersistable(HandOfDice handOfDice) {
        return handOfDice
                .stream()
                .map(String::valueOf)
                .collect(Collectors.joining(","));
    }
}
