package com.totalcraft.soled;

import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class Configs {
    public static File configFile;
    public static YamlConfiguration config;
    private Modules modules;
    private Main main;
    public static boolean rankupModule;
    public static boolean eventGroupChangeModule;
    public static String worldLocatinaMina = "spawn";
    public static int xLocatinaMina, yLocatinaMina, zLocatinaMina, x1Reset, x2Reset, y1Reset, y2Reset, z1Reset, z2Reset;
    public static List<String> blocks;
    public Configs(Main main) {
        this.main = main;
    }

    public void setConfigs() {
        configFile = new File(main.getDataFolder(), "config.yml");
        config = YamlConfiguration.loadConfiguration(configFile);
        blocks = config.getStringList("blocksReset");

        config.options().header("-- Plugin TTC --\n\nModules para desativar e ativar" +
                "\nLocalização da mina de Mundo e X Y Z" +
                "\nLocalização de X Y Z a X Y Z do Reset da mina" +
                "\nBlocos que serão setados no reset da Mina em Id:MetaData:%" +
                "\n");

        rankupModule = config.getBoolean("rankupModule");
        eventGroupChangeModule = config.getBoolean("eventGroupChangeModule");
        config.addDefault("rankupModule", rankupModule);
        config.addDefault("eventGroupChangeModule", eventGroupChangeModule);
        worldLocatinaMina = config.getString("worldLocationMina");
        if (worldLocatinaMina == null) {
            worldLocatinaMina = "spawn";
        }
        xLocatinaMina = config.getInt("xLocatinaMina");
        yLocatinaMina = config.getInt("yLocatinaMina");
        zLocatinaMina = config.getInt("zLocatinaMina");
        config.addDefault("worldLocationMina", worldLocatinaMina);
        config.addDefault("xLocatinaMina", xLocatinaMina);
        config.addDefault("yLocatinaMina", yLocatinaMina);
        config.addDefault("zLocatinaMina", zLocatinaMina);

        x1Reset = config.getInt("x1Reset");
        y1Reset = config.getInt("y1Reset");
        z1Reset = config.getInt("z1Reset");
        x2Reset = config.getInt("x2Reset");
        y2Reset = config.getInt("y2Reset");
        z2Reset = config.getInt("z2Reset");
        config.addDefault("x1Reset", x1Reset);
        config.addDefault("y1Reset", y1Reset);
        config.addDefault("z1Reset", z1Reset);
        config.addDefault("x2Reset", x2Reset);
        config.addDefault("y2Reset", y2Reset);
        config.addDefault("z2Reset", z2Reset);

        config.set("blocksReset", blocks);
        config.options().copyDefaults(true);
        try {
            config.save(configFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        modules = new Modules();
        modules.setMainInstance(this);
    }

    public void setRankupModule(boolean value) {
        rankupModule = value;
        config.set("rankupModule", value);
        try {
            config.save(configFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setEventGroupChangeModule(boolean value) {
        eventGroupChangeModule = value;
        config.set("eventGroupChangeModule", value);
        try {
            config.save(configFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
