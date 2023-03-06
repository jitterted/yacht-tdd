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

//     public static GameDatabase createNull() {
//         return new GameDatabase(new StubbedJpa(gameDatabaseJpa));
//     }

    @Autowired
    public GameDatabase(GameDatabaseJpa gameDatabaseJpa) {
        this(new RealJpa(gameDatabaseJpa));
    }

    private GameDatabase(Jpa gameDatabaseJpa) {
        this.gameDatabaseJpa = gameDatabaseJpa;
    }

    public static GameDatabase createNull() {
        return new GameDatabase(new StubbedJpa());
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
        @Override
        public GameTable save(GameTable gameTable) {
            return null;
        }

        @Override
        public Optional<GameTable> findById(Long id) {
            Game.Snapshot snapshot = new Game.Snapshot(
                    1,
                    false,
                    HandOfDice.of(1, 2, 3, 4, 5),
                    new Scoreboard.Snapshot(Collections.emptyMap()));
            return Optional.of(GameTable.from(snapshot));
        }
    }
}
