package org.example.scratchgame;

import java.util.Arrays;
import java.util.stream.Collectors;

public class ResultPrinter {

    public static void printResult(GameResult result) {
        System.out.println("Matrix:");
        System.out.println();
        for (String[] row : result.matrix()) {
            System.out.println(String.join(" | ", Arrays.stream(row)
                    .map(cell -> String.format("%-5s", cell))
                    .toArray(String[]::new)));
        }
        System.out.println();

        System.out.println("Reward: " + result.reward());

        System.out.println("Applied Winning Combinations: " +
                (result.appliedWinningCombinations().isEmpty() ? "None" :
                        result.appliedWinningCombinations().entrySet().stream()
                                .map(entry -> entry.getKey() + ": " + String.join(", ", entry.getValue()))
                                .collect(Collectors.joining(", "))
                ));

        System.out.println("Applied Bonus Symbol: " + (result.appliedBonusSymbol() != null ? result.appliedBonusSymbol() : "None"));
    }
}