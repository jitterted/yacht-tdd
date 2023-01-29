package com.jitterted.yacht.adapter.out.jpa;

import com.jitterted.yacht.domain.ScoreCategory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
class GameJpaRepositoryTest {

    @Autowired
    GameJpaRepository gameJpaRepository;

    @Test
    void canSaveAndFindGameDboToCrudRepository() {
        GameDbo gameDbo = new GameDbo();
        gameDbo.setId(42L);
        gameDbo.setRolls(2);
        gameDbo.setCurrentHand(List.of(6, 5, 4, 3, 2));
        gameDbo.setRoundCompleted(true);

        ScoredCategoryDbo scoredCategoryDbo1 = new ScoredCategoryDbo();
        scoredCategoryDbo1.setScoreCategory(ScoreCategory.FIVES);
        scoredCategoryDbo1.setHandOfDice(List.of(5, 5, 5, 5, 5));

        ScoredCategoryDbo scoredCategoryDbo2 = new ScoredCategoryDbo();
        scoredCategoryDbo2.setScoreCategory(ScoreCategory.FULLHOUSE);
        scoredCategoryDbo2.setHandOfDice(List.of(3, 3, 3, 4, 4));

        gameDbo.setScoredCategoryDbos(List.of(scoredCategoryDbo1,
                                              scoredCategoryDbo2));

        GameDbo savedGameDbo = gameJpaRepository.save(gameDbo);

        assertThat(savedGameDbo.getId())
                .isEqualTo(42L);

        assertThat(gameJpaRepository.findAll())
                .hasSize(1);

        assertThat(gameJpaRepository.findById(42L))
                .isNotEmpty();
    }

}