package com.totalcraft.soled.Configs;

import com.totalcraft.soled.Main;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class JailData {
    private final Main main;

    public JailData(Main main) {
        this.main = main;
    }

    public static File jailFile;
    public static YamlConfiguration jailConfig;
    public static HashMap<String, Integer> jailListPlayer = new HashMap<>();

    public void loadJailData() {
        jailFile = new File(main.getDataFolder(), "data/jail.yml");
        jailConfig = YamlConfiguration.loadConfiguration(jailFile);

        for (String playerName : jailConfig.getKeys(false)) {
            int timeLeft = jailConfig.getInt(playerName);
            jailListPlayer.put(playerName, timeLeft);
        }
    }

    public static void saveJailData() {
        for (String key : jailConfig.getKeys(false)) {
            if (jailListPlayer != null) {
                if (!jailListPlayer.containsKey(key)) {
                    jailConfig.set(key, null);
                }
            }
        }
        for (Map.Entry<String, Integer> entry : jailListPlayer.entrySet()) {
            jailConfig.set(entry.getKey(), entry.getValue());
        }

        try {
            jailConfig.save(jailFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
