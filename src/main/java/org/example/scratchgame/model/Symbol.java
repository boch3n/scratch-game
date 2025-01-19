package org.example.scratchgame.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public record Symbol(
        String name,
        @JsonProperty("reward_multiplier") Double rewardMultiplier,
        SymbolType type,
        Double extra,
        SymbolImpact impact) {

    public enum SymbolType {
        STANDARD, BONUS;

        @JsonCreator
        public static SymbolType fromString(String value) {
            return switch (value == null ? "" : value.toLowerCase()) {
                case "standard" -> STANDARD;
                case "bonus" -> BONUS;
                default -> throw new IllegalArgumentException("Unknown value: " + value);
            };
        }
    }

    public enum SymbolImpact {
        MULTIPLY_REWARD, EXTRA_BONUS, MISS;

        @JsonCreator
        public static SymbolImpact fromString(String value) {
            return switch (value == null ? "" : value.toLowerCase()) {
                case "multiply_reward" -> MULTIPLY_REWARD;
                case "extra_bonus" -> EXTRA_BONUS;
                case "miss" -> MISS;
                default -> throw new IllegalArgumentException("Unknown value: " + value);
            };
        }
    }
}
