package com.totalcraft.soled.PlayerManager;

import com.totalcraft.soled.Main;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class RewardData {
    public static File RewardFile;
    public static YamlConfiguration RewardConfig;
    public static Map<String, ItemStack> rewardDaily = new HashMap<>();
    public static Map<String, ItemStack> rewardWeekly = new HashMap<>();
    public static Map<String, ItemStack> rewardMonth = new HashMap<>();
    public static Map<String, ItemStack> rewardReset = new HashMap<>();

    public void loadRewardData() {
        RewardFile = new File(Main.get().getDataFolder(), "Recompensas.yml");
        RewardConfig = YamlConfiguration.loadConfiguration(RewardFile);
        RewardConfig.options().header("\nRecompensas por horas jogadas dos players\nTipos: Daily, Weekly, Month e Reset\n ");
        try {
            RewardConfig.save(RewardFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        loadData();
    }

    private void loadData() {
        loadReward("Daily", rewardDaily);
        loadReward("Weekly", rewardWeekly);
        loadReward("Month", rewardMonth);
        loadReward("Reset", rewardReset);
    }

    private void loadReward(String path, Map<String, ItemStack> reward) {
        Map<String, ItemStack> map = new HashMap<>();
        if (RewardConfig.contains(path)) {
            ConfigurationSection dailyCfg = RewardConfig.getConfigurationSection(path);
            for (String cfg : dailyCfg.getKeys(false)) {
                ConfigurationSection itemCfg = dailyCfg.getConfigurationSection(cfg);
                int hours = itemCfg.getInt("Horas");
                int id = itemCfg.getInt("ID");
                short meta = (short) itemCfg.getInt("Meta");
                int amount = itemCfg.getInt("Quantidade");
                String msg = itemCfg.getString("Mensagem");
                if (msg == null) msg = "";
                ItemStack itemStack = new ItemStack(id, amount, meta);
                map.put(hours + "-" + cfg + "-" + msg, itemStack);
            }
        }
        reward.putAll(map);
    }

}
