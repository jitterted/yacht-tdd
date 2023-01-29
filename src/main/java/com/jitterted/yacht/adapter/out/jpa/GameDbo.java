package com.jitterted.yacht.adapter.out.jpa;

import com.jitterted.yacht.domain.Game;
import com.jitterted.yacht.domain.ScoreCategory;
import com.jitterted.yacht.domain.Scoreboard;

import javax.persistence.CascadeType;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Entity
public class GameDbo {

    static final long THE_ONLY_ID = 42L;
    @Id
    private Long id;

    private boolean roundCompleted;

    private int rolls;

    @ElementCollection
    private List<Integer> currentHand;

    @OneToMany(cascade = CascadeType.ALL)
    private List<ScoredCategoryDbo> scoredCategoryDbos;

    static GameDbo from(Game game) {
        GameDbo gameDbo = new GameDbo();
        gameDbo.setId(THE_ONLY_ID);

        Game.Memento memento = game.memento();
        gameDbo.setRoundCompleted(memento.roundCompleted());
        gameDbo.setRolls(memento.rolls());
        gameDbo.setCurrentHand(memento.lastRoll());
        List<ScoredCategoryDbo> dboList =
                memento.scoreboard().scoredCategoryHandMap()
                       .entrySet()
                       .stream()
                       .map(entry -> {
                           ScoredCategoryDbo dbo = new ScoredCategoryDbo();
                           dbo.setScoreCategory(entry.getKey());
                           dbo.setHandOfDice(entry.getValue());
                           return dbo;
                       })
                       .toList();
        gameDbo.setScoredCategoryDbos(dboList);
        return gameDbo;
    }

    Game toDomain() {
        Map<ScoreCategory, List<Integer>> map = getScoredCategoryDbos()
                .stream()
                .collect(Collectors.toMap(ScoredCategoryDbo::getScoreCategory,
                                          ScoredCategoryDbo::getHandOfDice));
        Scoreboard.Memento scoreboardMemento =
                new Scoreboard.Memento(map);

        Game.Memento savedGameMemento = new Game.Memento(isRoundCompleted(),
                                                         getRolls(),
                                                         getCurrentHand(),
                                                         scoreboardMemento);
        return Game.from(savedGameMemento);
    }


    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public boolean isRoundCompleted() {
        return roundCompleted;
    }

    public void setRoundCompleted(boolean roundCompleted) {
        this.roundCompleted = roundCompleted;
    }

    public int getRolls() {
        return rolls;
    }

    public void setRolls(int rolls) {
        this.rolls = rolls;
    }

    public List<Integer> getCurrentHand() {
        return currentHand;
    }

    public void setCurrentHand(List<Integer> currentHand) {
        this.currentHand = currentHand;
    }

    public List<ScoredCategoryDbo> getScoredCategoryDbos() {
        return scoredCategoryDbos;
    }

    public void setScoredCategoryDbos(List<ScoredCategoryDbo> scoredCategoryDbos) {
        this.scoredCategoryDbos = scoredCategoryDbos;
    }
}
