package com.jitterted.yacht.application;

import com.jitterted.yacht.domain.Game;

public class InMemoryGameRepository {
    private Game.Snapshot gameSnapshot;

    public Game save(Game game) {
        this.gameSnapshot = game.memento();
        return Game.from(gameSnapshot);
    }

    public Game find() {
        return Game.from(gameSnapshot);
    }

}