package com.jitterted.yacht.adapter.out.gamedatabase;

import com.jitterted.yacht.adapter.OutputListener;
import com.jitterted.yacht.adapter.OutputTracker;
import com.jitterted.yacht.domain.Game;
import com.jitterted.yacht.domain.HandOfDice;
import com.jitterted.yacht.domain.Scoreboard;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.Optional;

@Repository
public class GameDatabase {

    static final Long THE_ONLY_GAME_ID = 777L;
    private final Jpa gameDatabaseJpa;
    private final OutputListener<Game.Snapshot> listener = new OutputListener<>();

    @Autowired
    public GameDatabase(GameDatabaseJpa gameDatabaseJpa) {
        this(new RealJpa(gameDatabaseJpa));
    }

    private GameDatabase(Jpa gameDatabaseJpa) {
        this.gameDatabaseJpa = gameDatabaseJpa;
    }

    public static GameDatabase createNull() {
        return new GameDatabase(StubbedJpa.asDefault());
    }

    public static GameDatabase createNull(Game.Snapshot configuredSnapshot) {
        return new GameDatabase(StubbedJpa.asConfigured(configuredSnapshot));
    }

    public static GameDatabase createCorruptedNull() {
        return new GameDatabase(StubbedJpa.asCorrupted());
    }

    public OutputTracker<Game.Snapshot> trackSaves() {
        return listener.createTracker();
    }

    public void saveGame(Game.Snapshot snapshot) {
        GameTable gameTable = GameTable.from(snapshot);

        gameDatabaseJpa.save(gameTable);
        listener.emit(snapshot);
    }

    public Optional<Game.Snapshot> loadGame() throws GameCorrupted {
        try {
            return gameDatabaseJpa
                    .findById(THE_ONLY_GAME_ID)
                    .map(GameTable::asSnapshot);
        } catch (GameCorruptedInternalException e) {
            throw new GameCorrupted(e.getMessage());
        }
    }


    // ----- NULLABILITY -----

    interface Jpa {
        GameTable save(GameTable gameTable);

        Optional<GameTable> findById(Long id);
    }

    private static class RealJpa implements Jpa {

        private final GameDatabaseJpa gameDatabaseJpa;

        public RealJpa(GameDatabaseJpa gameDatabaseJpa) {
            this.gameDatabaseJpa = gameDatabaseJpa;
        }

        @Override
        public GameTable save(GameTable gameTable) {
            return gameDatabaseJpa.save(gameTable);
        }

        @Override
        public Optional<GameTable> findById(Long id) {
            return gameDatabaseJpa.findById(id);
        }
    }

    private static class StubbedJpa implements Jpa {
        private final GameTable gameTable;

        public static StubbedJpa asDefault() {
            Game.Snapshot snapshot = new Game.Snapshot(
                    1,
                    false,
                    HandOfDice.of(1, 2, 3, 4, 5),
                    new Scoreboard.Snapshot(Collections.emptyMap()));

            return new StubbedJpa(GameTable.from(snapshot));
        }

        public static StubbedJpa asConfigured(Game.Snapshot snapshot) {
            return new StubbedJpa(GameTable.from(snapshot));
        }

        public static StubbedJpa asCorrupted() {
            GameTable corruptedGameTable = new GameTable();
            corruptedGameTable.setCurrentHand("invalid");
            return new StubbedJpa(corruptedGameTable);
        }

        public StubbedJpa(GameTable gameTable) {
            this.gameTable = gameTable;
        }

        @Override
        public GameTable save(GameTable gameTable) {
            return null;
        }

        @Override
        public Optional<GameTable> findById(Long id) {
            return Optional.of(gameTable);
        }
    }
}
