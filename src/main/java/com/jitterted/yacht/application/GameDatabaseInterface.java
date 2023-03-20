package com.jitterted.yacht.application;

import com.jitterted.yacht.adapter.out.gamedatabase.GameCorrupted;
import com.jitterted.yacht.domain.Game;

import java.util.Optional;

public interface GameDatabaseInterface {
    void saveGame(Game.Snapshot snapshot);

    Optional<Game.Snapshot> loadGame() throws GameCorrupted;
}
