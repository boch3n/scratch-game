package org.example.scratchgame;

import org.example.scratchgame.model.Symbol;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class MatrixGenerator {

    public static String[][] generateMatrix(GameConfig config) {
        int rows = config.rows();
        int columns = config.columns();
        String[][] matrix = new String[rows][columns];

        Map<String, Integer> probabilities = getProbabilities(config);

        fillMatrixWithSymbols(matrix, probabilities);

        ensureOneBonusSymbol(matrix, config);

        return matrix;
    }

    private static Map<String, Integer> getProbabilities(GameConfig config) {
        Map<String, Integer> flattenedProbabilities = new HashMap<>();

        config.probabilities().forEach((symbolType, symbolProbabilityList) ->
                symbolProbabilityList.forEach(symbolProbability -> {
                    flattenedProbabilities.putAll(symbolProbability.symbols());
                })
        );

        return flattenedProbabilities;
    }

    private static void fillMatrixWithSymbols(String[][] matrix, Map<String, Integer> probabilities) {
        IntStream.range(0, matrix.length).forEach(row ->
                IntStream.range(0, matrix[row].length).forEach(col ->
                        matrix[row][col] = generateSymbol(probabilities, new Random())
                )
        );
    }

    private static String generateSymbol(Map<String, Integer> symbolProbabilities, Random random) {
        List<String> symbols = new ArrayList<>(symbolProbabilities.keySet());
        List<Integer> probabilities = new ArrayList<>(symbolProbabilities.values());
        List<Double> normalizedProbabilities = normalizeProbabilities(probabilities);

        double randomValue = random.nextDouble();
        double cumulativeProbability = 0.0;

        for (int i = 0; i < symbols.size(); i++) {
            cumulativeProbability += normalizedProbabilities.get(i);
            if (randomValue <= cumulativeProbability) {
                return symbols.get(i);
            }
        }

        return symbols.getLast();
    }

    private static List<Double> normalizeProbabilities(List<Integer> probabilities) {
        double totalProbability = probabilities.stream().mapToDouble(Integer::doubleValue).sum();
        return probabilities.stream()
                .map(prob -> prob / totalProbability)
                .collect(Collectors.toList());
    }

    private static void ensureOneBonusSymbol(String[][] matrix, GameConfig config) {
        List<int[]> bonusSymbolPositions = IntStream.range(0, matrix.length)
                .boxed()
                .flatMap(row -> IntStream.range(0, matrix[row].length)
                        .filter(col -> isBonusSymbol(matrix[row][col], config))
                        .mapToObj(col -> new int[]{row, col}))
                .toList();

        if (bonusSymbolPositions.size() > 1) {
            Random random = new Random();
            int randomBonusIndex = random.nextInt(bonusSymbolPositions.size());
            Set<int[]> positionsToRegenerate = new HashSet<>(bonusSymbolPositions);
            positionsToRegenerate.remove(bonusSymbolPositions.get(randomBonusIndex));

            for (int[] position : positionsToRegenerate) {
                int row = position[0];
                int col = position[1];
                do {
                    matrix[row][col] = generateSymbol(getProbabilities(config), random);
                } while (isBonusSymbol(matrix[row][col], config));
            }
        }
    }

    private static boolean isBonusSymbol(String symbol, GameConfig config) {
        return config.symbols().containsKey(symbol) && config.symbols().get(symbol).type() == Symbol.SymbolType.BONUS;
    }

}
