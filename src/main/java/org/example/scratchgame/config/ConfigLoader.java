package org.example.scratchgame.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.scratchgame.GameConfig;
import org.example.scratchgame.application.ScratchGame;

import java.io.InputStream;

public class ConfigLoader {
    public static GameConfig loadConfig(String configFile) {
        try {
            InputStream inputStream = ScratchGame.class.getClassLoader().getResourceAsStream(configFile);

            if (inputStream == null) {
                System.out.println("Config file not found: " + configFile);
                return null;
            }

            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(inputStream, GameConfig.class);
        } catch (Exception e) {
            System.out.println("Error reading config file: " + e.getMessage());
            return null;
        }
    }
}