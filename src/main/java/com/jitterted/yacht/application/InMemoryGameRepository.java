package com.jitterted.yacht.application;

import com.jitterted.yacht.domain.Game;

import java.util.Optional;

public class InMemoryGameRepository {
    private Game.Snapshot gameSnapshot;

    public void saveGame(Game.Snapshot snapshot) {
        this.gameSnapshot = snapshot;
    }

    public Optional<Game.Snapshot> loadGame() {
        return Optional.of(gameSnapshot);
    }

}