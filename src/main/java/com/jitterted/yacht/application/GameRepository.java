package com.jitterted.yacht.application;

import com.jitterted.yacht.domain.Game;

public class GameRepository {
    private Game.Memento gameMemento;

    public void save(Game game) {
        this.gameMemento = game.memento();
    }

    public Game find() {
        return Game.from(gameMemento);
    }

}