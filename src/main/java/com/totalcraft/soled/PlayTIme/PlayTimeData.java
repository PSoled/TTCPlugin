package com.totalcraft.soled.PlayTIme;

import com.totalcraft.soled.Main;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.totalcraft.soled.PlayTIme.PlayTimeTask.DailyVerifyPT;
import static com.totalcraft.soled.PlayTIme.PlayerPT.PlayerPTData;

public class PlayTimeData {
    public static File PlayTimeFile;
    public static YamlConfiguration PlayTimeConfig;

    public void loadPlayTime() {
        PlayTimeFile = new File(Main.get().getDataFolder(), "data/PlayTime.yml");
        PlayTimeConfig = YamlConfiguration.loadConfiguration(PlayTimeFile);
        loadData();
    }

    private void loadData() {
        DailyVerifyPT = PlayTimeConfig.getBoolean("DailyVerify");
        PlayTimeConfig.set("DailyVerify", DailyVerifyPT);
        if (PlayTimeConfig.contains("PlayTime")) {
            ConfigurationSection PlayTime = PlayTimeConfig.getConfigurationSection("PlayTime");
            for (String nick : PlayTime.getKeys(false)) {
                ConfigurationSection player = PlayTime.getConfigurationSection(nick);
                long daily = player.getLong("Daily");
                long weekly = player.getLong("Weekly");
                long month = player.getLong("Month");
                long reset = player.getLong("Reset");
                long previousDaily = player.getLong("PreviousDaily");
                long previousWeekly = player.getLong("PreviousWeekly");
                long previousMonth = player.getLong("PreviousMonth");

                ConfigurationSection configReset = null;
                Object obj = player.get("ResetHistoric");
                if (!(obj instanceof String) || !((String) obj).equalsIgnoreCase("null")) {
                    configReset = player.getConfigurationSection("ResetHistoric");
                }
                List<Long> resetHistoric = new ArrayList<>();
                if (configReset != null) {
                    for (String historic : configReset.getKeys(false)) {
                        resetHistoric.add(configReset.getLong(historic));
                    }
                }
                PlayerPT playerPT = new PlayerPT(nick, daily, weekly, month, reset, previousDaily, previousWeekly, previousMonth, resetHistoric);
                PlayerPTData.put(nick, playerPT);
            }
            savePlayTimeFile();
        }
    }

    public static void savePlayTimeFile() {
        try {
            PlayTimeConfig.save(PlayTimeFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void savePlayTime() {
        PlayTimeConfig = YamlConfiguration.loadConfiguration(PlayTimeFile);
        for (Map.Entry<String, PlayerPT> entry : PlayerPTData.entrySet()) {
            PlayerPT playerPT = entry.getValue();
            PlayTimeConfig.set("PlayTime." + entry.getKey() + ".Daily", playerPT.getDaily());
            PlayTimeConfig.set("PlayTime." + entry.getKey() + ".Weekly", playerPT.getWeekly());
            PlayTimeConfig.set("PlayTime." + entry.getKey() + ".Month", playerPT.getMonth());
            PlayTimeConfig.set("PlayTime." + entry.getKey() + ".Reset", playerPT.getReset());
            PlayTimeConfig.set("PlayTime." + entry.getKey() + ".PreviousDaily", playerPT.getPreviousDaily());
            PlayTimeConfig.set("PlayTime." + entry.getKey() + ".PreviousWeekly", playerPT.getPreviousWeekly());
            PlayTimeConfig.set("PlayTime." + entry.getKey() + ".PreviousMonth", playerPT.getPreviousMonth());
            PlayTimeConfig.set("PlayTime." + entry.getKey() + ".ResetHistoric", "null");
            if (playerPT.getResetHistoric().size() > 0) {
                int num = 1;
                for (long reset : playerPT.getResetHistoric()) {
                    PlayTimeConfig.set("PlayTime." + entry.getKey() + ".ResetHistoric." + num, reset);
                    num++;
                }
            }
        }
        savePlayTimeFile();
    }

}
