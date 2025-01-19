package org.example.scratchgame.model;

import java.util.Map;

public record SymbolProbability(
        Integer column,
        Integer row,
        Map<String, Integer> symbols
) {}