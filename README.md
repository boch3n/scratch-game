# Scratch Game

## Overview
This is a simple scratch game implementation in Java. The game involves generating a matrix of symbols, checking for various winning combinations, and calculating rewards based on those combinations. The game also includes bonus symbols that can multiply or add to the reward.

## Features
- **Matrix Generation**: Randomly generates a matrix with symbols based on probabilities.
- **Winning Combinations**: Supports multiple winning combinations, including matching symbols, linear wins (horizontal, vertical, and diagonal), and bonus symbols.
- **Bonus Symbols**: Bonus symbols such as `10x`, `5x`, `+1000`, `+500`, and `MISS` are used to multiply or add to the total reward.
- **Reward Calculation**: Calculates the total reward based on the symbol occurrences and matching win combinations.

## Project Structure

- **`GameConfig`**: Configuration class for the game setup, including the number of rows and columns, symbols, probabilities, and win combinations.
- **`MatrixGenerator`**: Responsible for generating the matrix of symbols, ensuring one bonus symbol in the matrix, and applying probabilities to the symbols.
- **`RewardCalculator`**: Calculates the reward based on the generated matrix, applying the win combinations and bonus symbols.
- **`ProbabilitiesDeserializer`**: Custom deserializer for loading probabilities from the configuration file.
- **`Symbol` & `WinCombination`**: Models for representing the symbols used in the game and the various win combinations.

## Setup and Usage

### Dependencies
- **Jackson**: For JSON parsing and deserialization.
- **JUnit**: For unit testing.

### Running the Game
1. Prepare a valid configuration file `config.json` with the necessary symbols, probabilities, and win combinations.
2. Use the following command to run the game:

```bash
java -jar scratch-game-1.0-SNAPSHOT.jar --config config.json --betting-amount <bet_amount>
