package com.jitterted.yacht.application;

import com.jitterted.yacht.domain.Game;

public class InMemoryGameRepository {
    private Game.Snapshot gameSnapshot;

    public void saveGame(Game.Snapshot snapshot) {
        this.gameSnapshot = snapshot;
    }

    public Game loadGame() {
        return Game.from(gameSnapshot);
    }

}