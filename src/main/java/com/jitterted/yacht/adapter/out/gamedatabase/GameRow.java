package com.jitterted.yacht.adapter.out.gamedatabase;

import com.jitterted.yacht.domain.Game;
import com.jitterted.yacht.domain.HandOfDice;
import com.jitterted.yacht.domain.ScoreCategory;
import com.jitterted.yacht.domain.Scoreboard;

import javax.persistence.CollectionTable;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Entity
@Table(name = "games")
public class GameRow {
    @Id
    private Long id;

    private int rolls;

    private boolean roundCompleted;

    // couldn't get to work: https://vladmihalcea.com/postgresql-array-java-list/
    // private List<Integer> currentHand;
    private String currentHand;

    @ElementCollection
    @CollectionTable(name = "games_scoreboards")
    private Map<String, String> scoreboard;

    // -- from and asSnapshot are tested (indirectly) via GameDatabaseTest
    
    static GameRow from(Game.Snapshot snapshot) {
        GameRow gameRow = new GameRow();
        gameRow.setId(GameDatabase.THE_ONLY_GAME_ID);

        gameRow.setRolls(snapshot.rolls());
        gameRow.setRoundCompleted(snapshot.roundCompleted());
        gameRow.setCurrentHand(asPersistableHand(snapshot.currentHand()));
        gameRow.setScoreboard(asPersistableScoreboard(snapshot.scoreboard()));
        return gameRow;
    }

    Game.Snapshot asSnapshot() {
        return new Game.Snapshot(
                getRolls(),
                isRoundCompleted(),
                fromPersistedHand(getCurrentHand()),
                fromPersistedScoreboard(getScoreboard()));
    }

    private static Map<String, String> asPersistableScoreboard(Scoreboard.Snapshot scoreboard) {
        return scoreboard.scoredCategoryHandMap()
                         .entrySet()
                         .stream()
                         .collect(Collectors.toMap(
                                 entry -> entry.getKey().toString(),
                                 entry -> asPersistableHand(entry.getValue())));
    }

    private static String asPersistableHand(HandOfDice handOfDice) {
        return handOfDice
                .stream()
                .map(String::valueOf)
                .collect(Collectors.joining(","));
    }

    private static Scoreboard.Snapshot fromPersistedScoreboard(Map<String, String> scoreboardStrings) {
        Map<ScoreCategory, HandOfDice> map = scoreboardStrings
                .entrySet()
                .stream()
                .collect(Collectors.toMap(
                        entry -> fromPersistedScoreCategory(entry),
                        entry -> fromPersistedHand(entry.getValue()))
                );
        return new Scoreboard.Snapshot(map);
    }

    private static ScoreCategory fromPersistedScoreCategory(Map.Entry<String, String> entry) {
        try {
            return ScoreCategory.valueOf(entry.getKey());
        } catch (IllegalArgumentException e) {
            throw new GameCorruptedInternalException(
                    "Unrecognized ScoreCategory when loading game: " +
                            entry.getKey());
        }
    }

    private static HandOfDice fromPersistedHand(String handOfDice) {
        if (handOfDice.isBlank()) {
            return HandOfDice.unassigned();
        }

        List<Integer> integers;
        try {
            integers = Arrays.stream(handOfDice.split(","))
                             .map(Integer::parseInt)
                             .toList();
        } catch (NumberFormatException nfe) {
            throw new GameCorruptedInternalException(
                    "Invalid hand of dice when loading game: ["
                            + handOfDice + "]"
            );
        }

        return HandOfDice.from(integers, () -> {
            throw new GameCorruptedInternalException(
                    "Invalid hand of dice when loading game: ["
                            + handOfDice + "]");
        });
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public int getRolls() {
        return rolls;
    }

    public void setRolls(int rolls) {
        this.rolls = rolls;
    }

    public boolean isRoundCompleted() {
        return roundCompleted;
    }

    public void setRoundCompleted(boolean roundCompleted) {
        this.roundCompleted = roundCompleted;
    }

    public String getCurrentHand() {
        return currentHand;
    }

    public void setCurrentHand(String currentHand) {
        this.currentHand = currentHand;
    }

    public Map<String, String> getScoreboard() {
        return scoreboard;
    }

    public void setScoreboard(Map<String, String> scoreboard) {
        this.scoreboard = scoreboard;
    }

}
