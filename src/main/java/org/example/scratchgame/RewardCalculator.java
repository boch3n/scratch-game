package org.example.scratchgame;

import org.example.scratchgame.model.Symbol;
import org.example.scratchgame.model.WinCombination;

import java.util.*;

public class RewardCalculator {

    private static final Map<String, List<String>> appliedWinningCombinations = new HashMap<>();
    private static String appliedBonusSymbol = null;

    public static double calculateReward(String[][] matrix, GameConfig config, int betAmount) {
        double totalReward = 0;

        for (String symbol : config.symbols().keySet()) {
            double symbolReward = 0;

            List<Double> winMultipliers = getWinMultipliersForSymbol(symbol, matrix, config);

            if (!winMultipliers.isEmpty()) {
                double symbolMultiplier = getRewardMultiplier(symbol, config);
                symbolReward = betAmount * symbolMultiplier;

                for (double multiplier : winMultipliers) {
                    symbolReward *=multiplier;
                }
                totalReward += symbolReward;
            }
        }

        totalReward = applyBonusSymbol(totalReward, matrix, config);

        return totalReward;
    }

    private static List<Double> getWinMultipliersForSymbol(String symbol, String[][] matrix, GameConfig config) {
        int count = countSymbolOccurrences(symbol, matrix);

        List<Double> winMultipliers = new ArrayList<>();

        config.win_combinations().values().stream()
                .filter(winCombination -> winCombination.count() != null && winCombination.count() == count)
                .peek(winCombination -> {
                    appliedWinningCombinations
                            .computeIfAbsent(symbol, k -> new ArrayList<>())
                            .add("same_symbol_" + count + "_times");
                })
                .map(WinCombination::rewardMultiplier)
                .forEach(winMultipliers::add);

        checkForLinearWins(symbol, matrix, config, winMultipliers);

        return winMultipliers;
    }

    private static int countSymbolOccurrences(String symbol, String[][] matrix) {
        return (int) Arrays.stream(matrix)
                .flatMap(Arrays::stream)
                .filter(s -> s != null && s.equals(symbol))
                .count();
    }

    private static double getRewardMultiplier(String symbol, GameConfig config) {
        Symbol symbolObj = config.symbols().get(symbol);
        return symbolObj != null && symbolObj.rewardMultiplier() != null ? symbolObj.rewardMultiplier() : 1.0;
    }

    private static double applyBonusSymbol(double totalReward, String[][] matrix, GameConfig config) {
        for (Map.Entry<String, Symbol> entry : config.symbols().entrySet()) {
            String symbol = entry.getKey();
            Symbol symbolObj = entry.getValue();

            if (symbolObj.type() == Symbol.SymbolType.BONUS) {
                if (countSymbolOccurrences(symbol, matrix) > 0) {
                    Symbol.SymbolImpact impact = symbolObj.impact();
                    if (impact == Symbol.SymbolImpact.MULTIPLY_REWARD) {
                        totalReward *= symbolObj.rewardMultiplier();
                    } else if (impact == Symbol.SymbolImpact.EXTRA_BONUS) {
                        totalReward += symbolObj.extra();
                    }
                    appliedBonusSymbol = symbol;
                }
            }
        }

        return totalReward;
    }

    private static void checkForLinearWins(String symbol, String[][] matrix, GameConfig config, List<Double> winMultipliers) {
        for (WinCombination winCombination : config.win_combinations().values()) {
            if (winCombination.group() == WinCombination.Group.HORIZONTALLY_LINEAR_SYMBOLS) {
                if (checkWin(symbol, matrix, winCombination.coveredAreas())) {
                    winMultipliers.add(winCombination.rewardMultiplier());
                    appliedWinningCombinations
                            .computeIfAbsent(symbol, k -> new ArrayList<>())
                            .add("same_symbols_horizontally");
                }
            } else if (winCombination.group() == WinCombination.Group.VERTICALLY_LINEAR_SYMBOLS) {
                if (checkWin(symbol, matrix, winCombination.coveredAreas())) {
                    winMultipliers.add(winCombination.rewardMultiplier());
                    appliedWinningCombinations
                            .computeIfAbsent(symbol, k -> new ArrayList<>())
                            .add("same_symbols_vertically");
                }
            } else if (winCombination.group() == WinCombination.Group.LTR_DIAGONALLY_LINEAR_SYMBOLS) {
                if (checkWin(symbol, matrix, winCombination.coveredAreas())) {
                    winMultipliers.add(winCombination.rewardMultiplier());
                    appliedWinningCombinations
                            .computeIfAbsent(symbol, k -> new ArrayList<>())
                            .add("same_symbols_diagonally_left_to_right");
                }
            } else if (winCombination.group() == WinCombination.Group.RTL_DIAGONALLY_LINEAR_SYMBOLS) {
                if (checkWin(symbol, matrix, winCombination.coveredAreas())) {
                    winMultipliers.add(winCombination.rewardMultiplier());
                    appliedWinningCombinations
                            .computeIfAbsent(symbol, k -> new ArrayList<>())
                            .add("same_symbols_diagonally_right_to_left");
                }
            }
        }
    }

    private static boolean checkWin(String symbol, String[][] matrix, List<List<String>> coveredAreas) {
        for (List<String> area : coveredAreas) {
            boolean allMatch = area.stream().allMatch(cell -> {
                String[] parts = cell.split(":");
                int row = Integer.parseInt(parts[0]);
                int col = Integer.parseInt(parts[1]);
                String cellValue = matrix[row][col];
                return cellValue != null && cellValue.equals(symbol);
            });
            if (allMatch) return true;
        }
        return false;
    }

    public static Map<String, List<String>> getAppliedWinningCombinations() {
        return appliedWinningCombinations;
    }

    public static String getAppliedBonusSymbol() {
        return appliedBonusSymbol;
    }
}