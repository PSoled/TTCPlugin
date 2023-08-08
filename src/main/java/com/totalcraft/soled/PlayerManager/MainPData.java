package com.totalcraft.soled.PlayerManager;

import com.totalcraft.soled.Main;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class MainPData {
    public static File PDataFile;
    public static YamlConfiguration PDataConfig;
    public void LoadPData() {
        PDataFile = new File(Main.get().getDataFolder(), "data/PlayerData.yml");
        PDataConfig = YamlConfiguration.loadConfiguration(PDataFile);
        new DataLoad().loadData();
    }
}
