package com.jitterted.yacht.adapter.out.jpa;

import com.jitterted.yacht.application.GameRepository;
import com.jitterted.yacht.domain.Game;

import java.util.Optional;

public class GameRepositoryJpaAdapter implements GameRepository {
    private final GameJpaRepository gameJpaRepository;

    public GameRepositoryJpaAdapter(GameJpaRepository gameJpaRepository) {
        this.gameJpaRepository = gameJpaRepository;
    }

    @Override
    public Game save(Game game) {
        GameDbo gameDbo = GameDbo.from(game.memento());

        GameDbo savedGameDbo = gameJpaRepository.save(gameDbo);

        return savedGameDbo.toDomain();
    }

    @Override
    public Game find() {
        Optional<GameDbo> foundGame = gameJpaRepository
                .findById(GameDbo.THE_ONLY_ID);
        return foundGame.orElseThrow().toDomain();
    }
}
