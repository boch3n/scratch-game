package org.example.scratchgame.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record WinCombination(
        @JsonProperty("reward_multiplier") Double rewardMultiplier,
        Occurrence when,
        Integer count,
        Group group,
        @JsonProperty("covered_areas") List<List<String>> coveredAreas
) {

    public enum Occurrence {
        SAME_SYMBOLS, LINEAR_SYMBOLS;

        @JsonCreator
        public static Occurrence fromString(String value) {
            return switch (value == null ? "" : value.toLowerCase()) {
                case "same_symbols" -> SAME_SYMBOLS;
                case "linear_symbols" -> LINEAR_SYMBOLS;
                default -> throw new IllegalArgumentException("Unknown value: " + value);
            };
        }
    }

    public enum Group {
        SAME_SYMBOLS,
        HORIZONTALLY_LINEAR_SYMBOLS,
        VERTICALLY_LINEAR_SYMBOLS,
        LTR_DIAGONALLY_LINEAR_SYMBOLS,
        RTL_DIAGONALLY_LINEAR_SYMBOLS;

        @JsonCreator
        public static WinCombination.Group fromString(String value) {
            return switch (value == null ? "" : value.toLowerCase()) {
                case "same_symbols" -> SAME_SYMBOLS;
                case "horizontally_linear_symbols" -> HORIZONTALLY_LINEAR_SYMBOLS;
                case "vertically_linear_symbols" -> VERTICALLY_LINEAR_SYMBOLS;
                case "ltr_diagonally_linear_symbols" -> LTR_DIAGONALLY_LINEAR_SYMBOLS;
                case "rtl_diagonally_linear_symbols" -> RTL_DIAGONALLY_LINEAR_SYMBOLS;
                default -> throw new IllegalArgumentException("Unknown value: " + value);
            };
        }
    }
}
