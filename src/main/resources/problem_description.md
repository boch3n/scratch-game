# Home Assignment: Scratch Game #


## Requirements ##

- JDK >= 1.8
- Maven or Gradle
- feel free to choose any libraries for serialize/deserialize JSON and testing
- not recommended to add any additional libraries/frameworks, like spring or other high level frameworks

## Description ##

Problem statement: You need to build a scratch game, that will generate a matrix (for example 3x3) from symbols(based on probabilities for each individual cell) and based on winning combintations user either will win or lost.
User will place a bet with any amount which we call *betting amount* in this assignment.


There are two types of symbols: Standard Symbols, Bonus Symbols.

**Standard Symbols**: identifies if user won or lost the game based on winning combinations (combination can be X times repeated symbols or symbols that follow a specific pattern)
Bonus symbols are described the table below:


| Symbol Name | Reward Multiplier |
|-------------|-------------------|
| A           | 50                |
| B           | 25                |
| C           | 10                |
| D           | 5                 |
| E           | 3                 |
| F           | 1.5               |


**Bonus Symbols**: Bonus symbols are only effective when there are at least one winning combinations matches with the generated matrix. 
Bonus symbols are described the table below:

| Symbol Name | Action                       |
|-------------|------------------------------|
| 10x         | mutiply final reward to 10   |
| 5x          | mutiply final reward to 5    |
| +1000       | add 1000 to the final reward |
| +500        | add 500 to the final reward  |
| MISS        | none                         |


Let's look at the configuration file below:

```json
{
    "columns": 3, // OPTIONAL
    "rows": 3, // OPTIONAL
    "symbols": {
        "A": {
          "reward_multiplier": 5,
          "type": "standard"
        },
        "B": {
          "reward_multiplier": 3,
          "type": "standard"
        },
        "C": {
          "reward_multiplier": 2.5,
          "type": "standard"
        },
        "D": {
          "reward_multiplier": 2,
          "type": "standard"
        },
        "E": {
          "reward_multiplier": 1.2,
          "type": "standard"
        },
        "F": {
          "reward_multiplier": 1,
          "type": "standard"
        },
        "10x": {
          "reward_multiplier": 10,
          "type": "bonus",
          "impact": "multiply_reward"
        },
        "5x": {
          "reward_multiplier": 5,
          "type": "bonus",
          "impact": "multiply_reward"
        },
        "+1000": {
          "extra": 1000,
          "type": "bonus",
          "impact": "extra_bonus"
        },
        "+500": {
          "extra": 500,
          "type": "bonus",
          "impact": "extra_bonus"
        },
        "MISS": {
          "type": "bonus",
          "impact": "miss"
        }
    },
    "probabilities": {
        "standard_symbols": [
            {
            "column": 0,
            "row": 0,
            "symbols": {
                "A": 1,
                "B": 2,
                "C": 3,
                "D": 4,
                "E": 5,
                "F": 6
            }
            },
            {
            "column": 0,
            "row": 1,
            "symbols": {
                "A": 1,
                "B": 2,
                "C": 3,
                "D": 4,
                "E": 5,
                "F": 6
            }
            }
            //... please refer the config file (if not provided then copy from standard_symbols[0][0])
        ],
        "bonus_symbols": {
            "symbols": {
                "10x": 1,
                "5x": 2,
                "+1000": 3,
                "+500": 4,
                "MISS": 5
            }
        }
    },
    "win_combinations": {
        "same_symbol_3_times": {
            "reward_multiplier": 1,
            "when": "same_symbols",
            "count": 3,
            "group": "same_symbols"
        },
        "same_symbol_4_times": {
            "reward_multiplier": 1.5,
            "when": "same_symbols",
            "count": 4,
            "group": "same_symbols"
        },
        "same_symbol_5_times": {
            "reward_multiplier": 2,
            "when": "same_symbols",
            "count": 5,
            "group": "same_symbols"
        },
        "same_symbol_6_times": {
            "reward_multiplier": 3,
            "when": "same_symbols",
            "count": 6,
            "group": "same_symbols"
        },
        "same_symbol_7_times": {
            "reward_multiplier": 5,
            "when": "same_symbols",
            "count": 7,
            "group": "same_symbols"
        },
        "same_symbol_8_times": {
            "reward_multiplier": 10,
            "when": "same_symbols",
            "count": 8,
            "group": "same_symbols"
        },
        "same_symbol_9_times": {
            "reward_multiplier": 20,
            "when": "same_symbols",
            "count": 9,
            "group": "same_symbols"
        },

        "same_symbols_horizontally": { // OPTIONAL
            "reward_multiplier": 2,
            "when": "linear_symbols",
            "group": "horizontally_linear_symbols",
            "covered_areas": [
                ["0:0", "0:1", "0:2"],
                ["1:0", "1:1", "1:2"],
                ["2:0", "2:1", "2:2"]
            ]
        },
        "same_symbols_vertically": { // OPTIONAL
            "reward_multiplier": 2,
            "when": "linear_symbols",
            "group": "vertically_linear_symbols",
            "covered_areas": [
                ["0:0", "1:0", "2:0"],
                ["0:1", "1:1", "2:1"],
                ["0:2", "1:2", "2:2"]
            ]
        },
        "same_symbols_diagonally_left_to_right": { // OPTIONAL
            "reward_multiplier": 5,
            "when": "linear_symbols",
            "group": "ltr_diagonally_linear_symbols",
            "covered_areas": [
                ["0:0", "1:1", "2:2"]
            ]
        },
        "same_symbols_diagonally_right_to_left": { // OPTIONAL
            "reward_multiplier": 5,
            "when": "linear_symbols",
            "group": "rtl_diagonally_linear_symbols",
            "covered_areas": [
                ["0:2", "1:1", "2:0"]
            ]
        }
    }
}
```

| field name                                           | description                                                                                                                                                                                                               |
|------------------------------------------------------|---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| columns                                              | number of columns in the matrix                                                                                                                                                                                           |
| rows                                                 | number of rows in the matrix                                                                                                                                                                                              |
| symbols                                              | list of symbols                                                                                                                                                                                                           |
| symbol.{X}.reward_multiplier                         | will multiply betting amount                                                                                                                                                                                              |
| symbol.{X}.type                                      | can be either standard or bonus                                                                                                                                                                                           |
| symbol.{X}.extra                                     | [only for bonuses] extra amount which will be added to the reward                                                                                                                                                         |
| symbol.{X}.impact                                    | [only for bonuses] fixed values: multiply_reward (which multiply final reward to *symbol.{X}.reward_multiplier*), extra_bonus(will add *symbol.{X}.extra* to the final reward), miss(nothing)                             |
| probabilities                                        | list of probabilities                                                                                                                                                                                                     |
| probabilities.standard_symbols                       | list of probabilities for standard symbols                                                                                                                                                                                |
| probabilities.standard_symbols[...].column           | column index                                                                                                                                                                                                              |
| probabilities.standard_symbols[...].row              | row index                                                                                                                                                                                                                 |
| probabilities.standard_symbols[...].symbols          | map of a symbol and it's probability number(to calculate to probability percentage just sum all symbols probability numbers and divide individual symbol's probability number to total probability numbers)               |
| probabilities.bonus_symbols                          | list of probabilities for bonus symbols                                                                                                                                                                                   |
| probabilities.bonus_symbols.symbols                  | map of a symbol and it's probability number(to calculate to probability percentage just sum all symbols probability numbers and divide individual symbol's probability number to total probability numbers)               |
| probabilities.win_combinations                       | list of winning combinations                                                                                                                                                                                              |
| probabilities.win_combinations.{X}.reward_multiplier | will multiply reward                                                                                                                                                                                                      |
| probabilities.win_combinations.{X}.count             | required count of the same symbols to activate the reward                                                                                                                                                                 |
| probabilities.win_combinations.{X}.group             | group which the winning combination belongs to, max 1 winning combination should be applied for each win combination group                                                                                                |
| probabilities.win_combinations.{X}.covered_areas     | array of array of strings which is described as "%d:%d" which demonstrates row and column number respectively                                                                                                             |
| probabilities.win_combinations.{X}.when              | fixed values: same_symbols (if one symbol repeated in the matrix *probabilities.win_combinations.{X}.count* times), linear_symbols(if it matches to *probabilities.win_combinations.{X}.covered_areas*)                   |

- Note: Fields which are marked as OPTIONAL, are not required but will add extra points to the candidate if the candidate implements it.
- Note (2): Bonus symbol can be generated randomly in any cell(s) in the matrix
- Note (3): If one symbols matches more than winning combinations then reward should be multiplied. formula: (SYMBOL_1 * WIN_COMBINATION_1_FOR_SYMBOL_1 * WIN_COMBINATION_2_FOR_SYMBOL_1)
- Note (4): If the more than one symbols matches any winning combinations then reward should be summed. formula: (SYMBOL_1 * WIN_COMBINATION_1_FOR_SYMBOL_1 * WIN_COMBINATION_2_FOR_SYMBOL_1) + (SYMBOL_2 * WIN_COMBINATION_1_FOR_SYMBOL_2)

Input format:

```
    "bet_amount": 100
```

| field name | description    |
|------------|----------------|
| bet_amount | betting amount |

Output format:

```json
{
    "matrix": [
        ["A", "A", "B"],
        ["A", "+1000", "B"],
        ["A", "A", "B"]
    ],
    "reward": 6600,
    "applied_winning_combinations": {
        "A": ["same_symbol_5_times", "same_symbols_vertically"],
        "B": ["same_symbol_3_times", "same_symbols_vertically"]
    },
    "applied_bonus_symbol": "+1000"
}
```

| field name                   | description                                            |
|------------------------------|--------------------------------------------------------|
| matrix                       | generated 2D matrix                                    |
| reward                       | final reward which user won                            |
| applied_winning_combinations | Map of Symbol and List of applied winning combinations  |
| applied_bonus_symbol         | applied bonus symbol (can be null if the bonus is MISS |

Rewards breakdown:

| reward name             | reward details                    |
|-------------------------|-----------------------------------|
| symbol_A                | bet_amount x5                     |
| symbol_B                | bet_amount x3                     |
| same_symbol_5_times     | (reward for a specific symbol) x5 |
| same_symbol_3_times     | (reward for a specific symbol) x1 |
| same_symbols_vertically | (reward for a specific symbol) x2 |
| +1000                   | add 1000 extra to final reward    |

Calculations: (bet_amount x reward(symbol_A) x reward(same_symbol_5_times) x reward(same_symbols_vertically)) + (bet_amount x reward(symbol_B) x reward(same_symbol_3_times) x reward(same_symbols_vertically)) (+/x) reward(+1000) = (100 x5 x5 x2) + (100 x3 x1 x2) + 1000 = 5000 + 600 + 1000 = 6600

Examples (with a winning combination [same symbols should be repeated at least 3 / reward x2]):

Lost game:

![Lost Game](../../../../../Desktop/Dubai%202/take-home-assignment/lost_game.png "Lost")

| input             | output                                                                                                                      |
|-------------------|-----------------------------------------------------------------------------------------------------------------------------|
| "bet_amount": 100 | { </br> "matrix": [ </br> ["A", "B", "C"], </br> ["E", "B", "5x"], </br> ["F", "D", "C"] </br> ], </br> "reward": 0 </br> } |

Description: The game is settled as LOST, so bonus symbol has not been applied because the reward is 0.

Won game:

![Won Game](../../../../../Desktop/Dubai%202/take-home-assignment/won_game_3x3.png "Won with 10x")

| input             | output                                                                                                                                                                                                                                                   |
|-------------------|----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| "bet_amount": 100 | { </br> "matrix": [ </br> ["A", "B", "C"], </br> ["E", "B", "10x"], </br> ["F", "D", "B"] </br> ], </br> "reward": 50000, </br> "applied_winning_combinations": {</br> "B": ["same_symbol_3_times"] </br> }, </br> "applied_bonus_symbol": "10x" </br> } |

Description: user placed a bet with 100 betting amount and generated matrix has 3 same symbols which matches with the winning combination, also 10x bonus also will be applied.
Formula: 100(betting amount) x 3 | reward(symbol B) x1(at least 3 same symbols winning combination) x10(x10 bonus symbol) = 3000 (winning amount)

Note: Please make sure there are no errors while building (all test cases should be passed if you provided any) and your solution is testable through CLI like below:

```bash
 java -jar <your-jar-file> --config config.json --betting-amount 100
```

| parameter      | description                                        |
|----------------|----------------------------------------------------|
| config         | config file which is described top of the document |
| betting amount | betting amount                                     |