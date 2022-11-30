package com.jitterted.yacht.domain;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class YachtScorer {

    private static final HandOfDice LITTLE_STRAIGHT = HandOfDice.of(1, 2, 3, 4, 5);
    private static final HandOfDice BIG_STRAIGHT = HandOfDice.of(2, 3, 4, 5, 6);

    public int scoreAsOnes(HandOfDice roll) {
        return calculateScore(roll, 1);
    }

    public int scoreAsTwos(HandOfDice roll) {
        return calculateScore(roll, 2);
    }

    public int scoreAsThrees(HandOfDice roll) {
        return calculateScore(roll, 3);
    }

    public int scoreAsFours(HandOfDice roll) {
        return calculateScore(roll, 4);
    }

    public int scoreAsFives(HandOfDice roll) {
        return calculateScore(roll, 5);
    }

    public int scoreAsSixes(HandOfDice roll) {
        return calculateScore(roll, 6);
    }

    public int scoreAsFullHouse(HandOfDice roll) {
        if (!isValidFullHouse(roll)) {
            return 0;
        }

        return roll.stream()
                   .distinct()
                   .mapToInt(die -> calculateScore(roll, die))
                   .sum();
    }

    private boolean isValidFullHouse(HandOfDice roll) {
        var dieToCountMap = createDieToCountMap(roll);

        long numberOfDiceOccurringTwoOrThreeTimes = countForDieOccurringTwoOrThreeTimes(dieToCountMap);

        return hasTwoUniqueDice(dieToCountMap)
                && numberOfDiceOccurringTwoOrThreeTimes == 2;
    }

    private Map<Integer, Long> createDieToCountMap(HandOfDice roll) {
        return roll.stream()
                   .collect(
                           Collectors.groupingBy(
                                   Function.identity(),
                                   Collectors.counting()
                           )
                   );
    }

    private boolean hasTwoUniqueDice(Map<Integer, Long> dieToCountMap) {
        return dieToCountMap.size() == 2;
    }

    private long countForDieOccurringTwoOrThreeTimes(Map<Integer, Long> dieToCountMap) {
        return dieToCountMap.entrySet()
                            .stream()
                            .filter(this::twoOrThreeOccurrences)
                            .count();
    }

    private boolean twoOrThreeOccurrences(Map.Entry<Integer, Long> e) {
        return e.getValue() == 2 || e.getValue() == 3;
    }

    private int calculateScore(HandOfDice dice, int scoreCategory) {
        return dice.countFor(scoreCategory) * scoreCategory;
    }

    public int scoreAsFourOfAKind(HandOfDice handOfDice) {
        return calculateScoreOfAKind(handOfDice, 4);
    }

    public int scoreAsYacht(HandOfDice handOfDice) {
        return calculateScoreOfAKind(handOfDice, 5);
    }

    private int calculateScoreOfAKind(HandOfDice handOfDice, int kind) {
        return createDieToCountMap(handOfDice).entrySet()
                                              .stream()
                                              .filter(die -> occursAtLeast(kind, die))
                                              .mapToInt(die -> multiply(kind, die))
                                              .sum();
    }

    public int scoreAsLittleStraight(HandOfDice handOfDice) {
        return handOfDice.equals(LITTLE_STRAIGHT) ? 30 : 0;
    }

    public int scoreAsBigStraight(HandOfDice handOfDice) {
        return handOfDice.equals(BIG_STRAIGHT) ? 30 : 0;
    }

    public int scoreAsChoice(HandOfDice handOfDice) {
        return handOfDice.stream().mapToInt(Integer::intValue).sum();
    }

    private int multiply(int kind, Map.Entry<Integer, Long> die) {
        return die.getKey() * kind;
    }

    private boolean occursAtLeast(int kind, Map.Entry<Integer, Long> die) {
        return die.getValue() >= kind;
    }

}
