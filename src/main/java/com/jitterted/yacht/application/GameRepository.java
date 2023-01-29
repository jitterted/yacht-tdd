package com.jitterted.yacht.application;

import com.jitterted.yacht.domain.Game;

public interface GameRepository {
    void save(Game game);

    Game find();
}
