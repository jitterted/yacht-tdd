package com.jitterted.yacht.application;

import com.jitterted.yacht.domain.Game;

public class InMemoryGameRepository implements GameRepository {
    private Game.Memento gameMemento;

    @Override
    public Game save(Game game) {
        this.gameMemento = game.memento();
        return Game.from(gameMemento);
    }

    @Override
    public Game find() {
        return Game.from(gameMemento);
    }

}