package example.scratchgame;

import org.example.scratchgame.*;
import org.example.scratchgame.model.Symbol;
import org.example.scratchgame.model.SymbolProbability;
import org.example.scratchgame.model.WinCombination;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class RewardCalculatorTest {

    private GameConfig config;

    @BeforeEach
    void setUp() {
        Map<String, Symbol> symbols = new HashMap<>();
        symbols.put("A", new Symbol("A", 5.0, Symbol.SymbolType.STANDARD, null, null));
        symbols.put("B", new Symbol("B", 3.0, Symbol.SymbolType.STANDARD, null, null));
        symbols.put("C", new Symbol("C", 2.5, Symbol.SymbolType.STANDARD, null, null));
        symbols.put("D", new Symbol("D", 2.0, Symbol.SymbolType.STANDARD, null, null));
        symbols.put("E", new Symbol("E", 1.2, Symbol.SymbolType.STANDARD, null, null));
        symbols.put("F", new Symbol("F", 1.0, Symbol.SymbolType.STANDARD, null, null));
        symbols.put("10x", new Symbol("10x", 10.0, Symbol.SymbolType.BONUS, null, Symbol.SymbolImpact.MULTIPLY_REWARD));
        symbols.put("5x", new Symbol("5x", 5.0, Symbol.SymbolType.BONUS, null, Symbol.SymbolImpact.MULTIPLY_REWARD));
        symbols.put("+1000", new Symbol("+1000", null, Symbol.SymbolType.BONUS, 1000.0, Symbol.SymbolImpact.EXTRA_BONUS));
        symbols.put("+500", new Symbol("+500", null, Symbol.SymbolType.BONUS, 500.0, Symbol.SymbolImpact.EXTRA_BONUS));
        symbols.put("MISS", new Symbol("MISS", null, Symbol.SymbolType.BONUS, null, Symbol.SymbolImpact.MISS));

        Map<String, List<SymbolProbability>> probabilities = Map.of();

        Map<String, WinCombination> winCombinations = new HashMap<>();

        winCombinations.put("same_symbol_3_times", new WinCombination(
                1.0, WinCombination.Occurrence.SAME_SYMBOLS, 3, WinCombination.Group.SAME_SYMBOLS, null));
        winCombinations.put("same_symbol_4_times", new WinCombination(
                1.5, WinCombination.Occurrence.SAME_SYMBOLS, 4, WinCombination.Group.SAME_SYMBOLS, null));
        winCombinations.put("same_symbol_5_times", new WinCombination(
                2.0, WinCombination.Occurrence.SAME_SYMBOLS, 5, WinCombination.Group.SAME_SYMBOLS, null));
        winCombinations.put("same_symbol_6_times", new WinCombination(
                3.0, WinCombination.Occurrence.SAME_SYMBOLS, 6, WinCombination.Group.SAME_SYMBOLS, null));
        winCombinations.put("same_symbol_7_times", new WinCombination(
                5.0, WinCombination.Occurrence.SAME_SYMBOLS, 7, WinCombination.Group.SAME_SYMBOLS, null));
        winCombinations.put("same_symbol_8_times", new WinCombination(
                10.0, WinCombination.Occurrence.SAME_SYMBOLS, 8, WinCombination.Group.SAME_SYMBOLS, null));
        winCombinations.put("same_symbol_9_times", new WinCombination(
                20.0, WinCombination.Occurrence.SAME_SYMBOLS, 9, WinCombination.Group.SAME_SYMBOLS, null));
        winCombinations.put("same_symbols_horizontally", new WinCombination(
                2.0, WinCombination.Occurrence.LINEAR_SYMBOLS, null, WinCombination.Group.HORIZONTALLY_LINEAR_SYMBOLS, List.of(List.of("0:0", "0:1", "0:2"), List.of("1:0", "1:1", "1:2"), List.of("2:0", "2:1", "2:2"))));
        winCombinations.put("same_symbols_vertically", new WinCombination(
                2.0, WinCombination.Occurrence.LINEAR_SYMBOLS, null, WinCombination.Group.VERTICALLY_LINEAR_SYMBOLS, List.of(List.of("0:0", "1:0", "2:0"), List.of("0:1", "1:1", "2:1"), List.of("0:2", "1:2", "2:2"))));
        winCombinations.put("same_symbols_diagonally_left_to_right", new WinCombination(
                5.0, WinCombination.Occurrence.LINEAR_SYMBOLS, null, WinCombination.Group.LTR_DIAGONALLY_LINEAR_SYMBOLS, List.of(List.of("0:0", "1:1", "2:2"))));
        winCombinations.put("same_symbols_diagonally_right_to_left", new WinCombination(
                5.0, WinCombination.Occurrence.LINEAR_SYMBOLS, null, WinCombination.Group.RTL_DIAGONALLY_LINEAR_SYMBOLS, List.of(List.of("0:2", "1:1", "2:0"))));


        config = new GameConfig(3, 3, symbols, probabilities, winCombinations);
    }

    @Test
    void testCalculateRewardWithWinningCombination() {
        String[][] matrix = {
                {"A", "B", "C"},
                {"C", "B", "A"},
                {"C", "D", "B"}
        };

        int betAmount = 100;
        // 100 (bet) * 3 (reward for B) * 1 (multiplier for 3 B's) + 100 * 2.5 * 1
        double expectedReward = 550;

        double reward = RewardCalculator.calculateReward(matrix, config, betAmount);

        assertEquals(expectedReward, reward);
    }

    @Test
    void testCalculateRewardWithoutWinningCombination() {
        String[][] matrix = {
                {"A", "B", "C"},
                {"E", "B", "5x"},
                {"F", "D", "C"}
        };

        int betAmount = 100;
        int expectedReward = 0;

        double reward = RewardCalculator.calculateReward(matrix, config, betAmount);

        assertEquals(expectedReward, reward);
    }

    @Test
    void testCalculateRewardWithBonusSymbol() {
        String[][] matrix = {
                {"A", "A", "B"},
                {"B", "B", "10x"},
                {"C", "D", "E"}
        };

        int betAmount = 100;
        // 100 (bet) * 3 (reward for B) * 1 (multiplier for 3 B's) * 10x (bonus symbol)
        int expectedReward = 3000;

        double reward = RewardCalculator.calculateReward(matrix, config, betAmount);

        assertEquals(expectedReward, reward);
    }

    @Test
    void testNoAppliedBonusSymbol() {
        String[][] matrix = {
                {"A", "B", "C"},
                {"B", "A", "MISS"},
                {"C", "A", "E"}
        };

        int betAmount = 100;
        // 100 (bet) * 5 (reward for A) * 1 (multiplier for A) * 10x + 500
        int expectedReward = 500;

        double reward = RewardCalculator.calculateReward(matrix, config, betAmount);

        assertEquals(expectedReward, reward);
    }

    @Test
    void testCalculateRewardWithHorizontalWin() {
        String[][] matrix = {
                {"A", "A", "A"},
                {"B", "D", "E"},
                {"E", "F", "F"}
        };

        int betAmount = 100;
        // 100 (bet) * 5 (reward for A) * 1 (multiplier for 3 A's) * 2 (multiplier for horizontal)
        double expectedReward = 1000;

        double reward = RewardCalculator.calculateReward(matrix, config, betAmount);

        assertEquals(expectedReward, reward);
    }

    @Test
    void testCalculateRewardWithVerticalWin() {
        String[][] matrix = {
                {"E", "B", "D"},
                {"F", "B", "F"},
                {"D", "B", "E"}
        };

        int betAmount = 100;
        // 100 (bet) * 3 (reward for B) * 1 (multiplier for 3 B's) * 2 (multiplier for vertical)
        double expectedReward = 600;

        double reward = RewardCalculator.calculateReward(matrix, config, betAmount);

        assertEquals(expectedReward, reward);
    }

    @Test
    void testCalculateRewardWithDiagonalWinLeftToRight() {
        String[][] matrix = {
                {"A", "B", "C"},
                {"B", "A", "D"},
                {"C", "D", "A"}
        };

        int betAmount = 100;
        // 100 (bet) * 5 (reward for A) * 5 (multiplier for diagonal win)
        double expectedReward = 2500;

        double reward = RewardCalculator.calculateReward(matrix, config, betAmount);

        assertEquals(expectedReward, reward);
    }

    @Test
    void testCalculateRewardWithDiagonalWinRightToLeft() {
        String[][] matrix = {
                {"C", "B", "A"},
                {"B", "A", "D"},
                {"A", "D", "C"}
        };

        int betAmount = 100;
        // 100 (bet) * 5 (reward for A) * 5 (multiplier for diagonal win)
        double expectedReward = 2500;

        double reward = RewardCalculator.calculateReward(matrix, config, betAmount);

        assertEquals(expectedReward, reward);
    }

    @Test
    void testEdgeCaseEmptyMatrix() {
        // Edge case with an empty matrix (no symbols)
        String[][] matrix = new String[3][3]; // All null or empty values

        int betAmount = 100;
        int expectedReward = 0; // No matching combination, reward should be 0

        double reward = RewardCalculator.calculateReward(matrix, config, betAmount);

        assertEquals(expectedReward, reward);
    }
}
