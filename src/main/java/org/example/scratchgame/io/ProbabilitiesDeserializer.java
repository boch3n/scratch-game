package org.example.scratchgame.io;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.scratchgame.model.SymbolProbability;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ProbabilitiesDeserializer extends JsonDeserializer<List<SymbolProbability>> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public List<SymbolProbability> deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        JsonNode rootNode = objectMapper.readTree(p);
        List<SymbolProbability> probabilities = new ArrayList<>();

        if (rootNode.isArray()) {
            rootNode.forEach(symbolData -> {
                Integer row = symbolData.has("row") ? symbolData.get("row").asInt() : null;
                Integer column = symbolData.has("column") ? symbolData.get("column").asInt() : null;
                Map<String, Integer> symbols = objectMapper.convertValue(symbolData.get("symbols"), Map.class);
                probabilities.add(new SymbolProbability(row, column, symbols));
            });
        } else {
                Map<String, Integer> bonusSymbols = objectMapper.convertValue(rootNode.get("symbols"), Map.class);
                probabilities.add(new SymbolProbability(null, null, bonusSymbols));
        }
        return probabilities;
    }
}