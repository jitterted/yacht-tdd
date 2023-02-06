package com.jitterted.yacht.adapter.out.gamedatabase;

import com.jitterted.yacht.domain.Game;
import com.jitterted.yacht.domain.HandOfDice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

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
        gameTable.setCurrentHand(serializableStringOf(snapshot.currentHand()));

        gameDatabaseJpa.save(gameTable);
    }

    private static String serializableStringOf(HandOfDice handOfDice) {
        return handOfDice
                       .stream()
                       .map(String::valueOf)
                       .collect(Collectors.joining(","));
    }
}
