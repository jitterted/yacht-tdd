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
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@Tag("spring")
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
//@Transactional(propagation = Propagation.NOT_SUPPORTED)
public class GameDatabaseTest {

    @Autowired
    DataSource dataSource;

    @Autowired
    EntityManager entityManager;

    @Autowired
    GameDatabaseJpa gameDatabaseJpa;

//    @AfterEach
//    void deleteAllRows() throws Exception {
//        Connection connection = dataSource.getConnection();
//        connection.prepareStatement("TRUNCATE TABLE games").execute();
//    }

    @Test
    void writesToDatabase() {
        GameDatabase gameDatabase = new GameDatabase(gameDatabaseJpa);
        Game.Snapshot snapshot = new Game.Snapshot(true, 2, List.of(3, 4, 4, 5, 6), null);

        gameDatabase.saveGame(snapshot);

        Query query = entityManager.createNativeQuery("SELECT * FROM games");
        List<Object[]> games = query.getResultList();
        for (Object[] game : games) {
            System.out.println(Arrays.toString(game));
        }
        assertThat(games.get(0)) // first row
                .contains(true, 2);
        assertThat(games) // number of rows
                .hasSize(1);

//        Table table = new Table(dataSource, "games");
//        Assertions.assertThat(table)
//                  .hasNumberOfRows(1);

//        Assertions.assertThat(table)
//                  .hasNumberOfColumns(25);

//        Assertions.assertThat(table).column("id")
//                  .value().isGreaterThan(0L);
//
//        Assertions.assertThat(table).column("round_completed")
//                  .value().isTrue();
//        Assertions.assertThat(table).column("rolls")
//                  .value().isEqualTo(2);
//        Assertions.assertThat(table).column("current_hand")
//                  .value().isEqualTo("xxx");

    }


}
