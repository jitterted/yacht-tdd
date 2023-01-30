package com.jitterted.yacht.application;

import com.jitterted.yacht.domain.Game;

public class InMemoryGameRepository implements GameRepository {
    private Game.Snapshot gameSnapshot;

    @Override
    public Game save(Game game) {
        this.gameSnapshot = game.memento();
        return Game.from(gameSnapshot);
    }

    @Override
    public Game find() {
        return Game.from(gameSnapshot);
    }

}