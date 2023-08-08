package com.totalcraft.soled.Configs;

import com.totalcraft.soled.Main;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class ItemPrivateLog {
    private final Main main;
    public ItemPrivateLog(Main main) {
        this.main = main;
    }
    public static File logITemFile;
    public static YamlConfiguration logItemConfig;
    public static HashMap<String, String> itemsPrivate = new HashMap<>();

    public void loadLogItem() {
        logITemFile = new File(main.getDataFolder(), "log/itemprivate.yml");
        logItemConfig = YamlConfiguration.loadConfiguration(logITemFile);
        logItemConfig.options().header("log de Ultimo uso de um Item Privado");

        for (String list : logItemConfig.getKeys(false)) {
            String log = logItemConfig.getString(list);
            itemsPrivate.put(list, log);
        }
    }

    public static void saveLogItem() {
        for (Map.Entry<String, String> entry : itemsPrivate.entrySet()) {
            logItemConfig.set(entry.getKey(), entry.getValue());
        }
        try {
            logItemConfig.save(logITemFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static ScheduledExecutorService schedulerLog = Executors.newScheduledThreadPool(1);
    public static ScheduledFuture<?> scheduledLog;
    public static void autoSaveLog() {
        scheduledLog = schedulerLog.scheduleAtFixedRate(ItemPrivateLog::saveLogItem, 0, 1, TimeUnit.MINUTES);
    }
}
