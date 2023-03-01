package com.totalcraft.soled.Configs;

import com.totalcraft.soled.Main;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class BflyData {
    private final Main main;
    public BflyData(Main main) {
        this.main = main;
    }
    public static File flyFile;
    public static YamlConfiguration flyConfig;
    public static HashMap<String, Integer> flyListPlayer = new HashMap<>();

    public void loadFlyData() {
        flyFile = new File(main.getDataFolder(), "fly.yml");
        flyConfig = YamlConfiguration.loadConfiguration(flyFile);

        for (String playerName : flyConfig.getKeys(false)) {
            int timeLeft = flyConfig.getInt(playerName);
            flyListPlayer.put(playerName, timeLeft);
        }
    }
    public static void saveFlyData() {
        for (String key : flyConfig.getKeys(false)) {
            if (!BflyData.flyListPlayer.containsKey(key)) {
                flyConfig.set(key, null);
            }
        }
        for (Map.Entry<String, Integer> entry : flyListPlayer.entrySet()) {
            flyConfig.set(entry.getKey(), entry.getValue());
        }

        try {
            flyConfig.save(flyFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
