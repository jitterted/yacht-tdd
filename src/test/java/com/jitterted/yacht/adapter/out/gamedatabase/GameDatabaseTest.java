package com.jitterted.yacht.adapter.out.gamedatabase;

import com.jitterted.yacht.domain.Game;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.sql.DataSource;
import java.math.BigInteger;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SuppressWarnings("unchecked")
@Tag("spring")
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class GameDatabaseTest {

    @Autowired
    DataSource dataSource;

    @Autowired
    EntityManager entityManager;

    @Autowired
    GameDatabaseJpa gameDatabaseJpa;

    @Test
    void writesCoreGameStateToDatabase() {
        GameDatabase gameDatabase = new GameDatabase(gameDatabaseJpa);
        Game.Snapshot snapshot = new Game.Snapshot(2,
                                                   true,
                                                   List.of(3, 4, 4, 5, 6),
                                                   null);

        gameDatabase.saveGame(snapshot);

        // from https://thorben-janssen.com/jpa-native-queries/
        Query query = entityManager.createNativeQuery(
                "SELECT id, rolls, round_completed, current_hand FROM games");
        List<Object[]> gameRows = query.getResultList();

        assertThat(gameRows)
                .containsExactly(new Object[]{
                        BigInteger.valueOf(1),
                        2,
                        true,
                        "3,4,4,5,6"
                });
    }

    @Test
    void writesScoreboardStateToDatabase() {
        GameDatabase gameDatabase = new GameDatabase(gameDatabaseJpa);
//        Scoreboard.Snapshot snapshot = new Scoreboard.Snapshot(
//                Map.of(ScoreCategory.FOURS, HandOfDice.of(1,2,4,4,4).
    }
}
