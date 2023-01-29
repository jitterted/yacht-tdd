package com.jitterted.yacht.application;

import com.jitterted.yacht.domain.Game;

public interface GameRepository {
    Game save(Game game);

    Game find();
}
