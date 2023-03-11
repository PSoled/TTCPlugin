package com.totalcraft.soled.Configs;

import com.totalcraft.soled.Main;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class BanItemCfg {
    private final Main main;

    public BanItemCfg(Main main) {
        this.main = main;
    }
    public static File banItemFile;
    public static YamlConfiguration banItemConfig;
    public static List<String> banItemList;

    public void loadBanItem() {
        banItemFile = new File(main.getDataFolder(), "banitem.yml");
        banItemConfig = YamlConfiguration.loadConfiguration(banItemFile);
        banItemList = banItemConfig.getStringList("ItensBans");
    }

    public static void saveBanItem() {
        banItemConfig.set("ItensBans", banItemList);
        try {
            banItemConfig.save(banItemFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

