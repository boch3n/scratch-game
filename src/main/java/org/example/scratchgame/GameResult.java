package org.example.scratchgame;

import java.util.List;
import java.util.Map;

public record GameResult(
        String[][] matrix,
        double reward,
        Map<String, List<String>> appliedWinningCombinations,
        String appliedBonusSymbol
) {}