package org.example.scratchgame.application;

import org.example.scratchgame.*;
import org.example.scratchgame.config.ConfigLoader;

public class ScratchGame {
    public static void main(String[] args) {

        InputArguments inputArgs = validateAndParseInput(args);
        if (inputArgs == null) {
            return;
        }

        GameConfig config = ConfigLoader.loadConfig(inputArgs.configFile());
        if (config == null) {
            return;
        }

        String[][] matrix = MatrixGenerator.generateMatrix(config);

        double reward = RewardCalculator.calculateReward(matrix, config, inputArgs.betAmount());

        GameResult result = new GameResult(matrix, reward, RewardCalculator.getAppliedWinningCombinations(), RewardCalculator.getAppliedBonusSymbol());

        ResultPrinter.printResult(result);
    }

    private static InputArguments validateAndParseInput(String[] args) {
        String configFile = null;
        int betAmount = 0;

        if (args.length != 4) {
            System.out.println("Usage: java -jar scratch-game-1.0-SNAPSHOT.jar --config <config_file> --betting-amount <bet_amount>");
            return null;
        }

        for (int i = 0; i < args.length; i++) {
            if ("--config".equals(args[i])) {
                if (i + 1 < args.length) {
                    configFile = args[i + 1];
                    i++; // Skip next argument
                } else {
                    System.out.println("Error: Missing config file path.");
                    return null;
                }
            } else if ("--betting-amount".equals(args[i])) {
                if (i + 1 < args.length) {
                    try {
                        betAmount = Integer.parseInt(args[i + 1]);
                        i++; // Skip next argument
                    } catch (NumberFormatException e) {
                        System.out.println("Error: Invalid betting amount.");
                        return null;
                    }
                } else {
                    System.out.println("Error: Missing betting amount.");
                    return null;
                }
            }
        }

        if (configFile == null) {
            System.out.println("Error: Missing --config argument.");
            return null;
        }

        return new InputArguments(configFile, betAmount);
    }
}