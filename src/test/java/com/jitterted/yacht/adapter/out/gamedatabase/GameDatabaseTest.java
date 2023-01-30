package com.jitterted.yacht.adapter.out.gamedatabase;

import com.jitterted.yacht.domain.Game;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
public class GameDatabaseTest {

    @Autowired
    DataSource dataSource;

    @Autowired
    GameDatabaseJpa gameDatabaseJpa;

    @Test
    void writesToDatabase() throws SQLException {
        GameDatabase gameDatabase = new GameDatabase(gameDatabaseJpa);

        Game.Snapshot snapshot = new Game.Snapshot(true, 2, List.of(3, 4, 4, 5, 6), null);

        gameDatabase.saveGame(snapshot);

        Connection connection = dataSource.getConnection();
        ResultSet resultSet = connection.prepareStatement("select * from games").executeQuery();

        resultSet.next();
        int rolls_column = resultSet.findColumn("rolls");
        assertThat(resultSet.getInt(rolls_column))
                .isEqualTo(2);
    }
}
