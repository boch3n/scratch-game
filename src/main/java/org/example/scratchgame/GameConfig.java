package org.example.scratchgame;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.example.scratchgame.io.ProbabilitiesDeserializer;
import org.example.scratchgame.model.Symbol;
import org.example.scratchgame.model.SymbolProbability;
import org.example.scratchgame.model.WinCombination;

import java.util.List;
import java.util.Map;

public record GameConfig(
        int rows,
        int columns,
        Map<String, Symbol> symbols,
        @JsonDeserialize(contentUsing = ProbabilitiesDeserializer.class)
        Map<String, List<SymbolProbability>> probabilities,
        Map<String, WinCombination> win_combinations
) {}