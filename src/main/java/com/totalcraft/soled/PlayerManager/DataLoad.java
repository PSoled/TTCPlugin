package com.totalcraft.soled.PlayerManager;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

import static com.totalcraft.soled.PlayerManager.MainPData.PDataConfig;
import static com.totalcraft.soled.PlayerManager.PlayerBase.playersBase;

public class DataLoad {
    public void loadData() {
        if (PDataConfig.contains("PlayersData")) {
            ConfigurationSection playersData = PDataConfig.getConfigurationSection("PlayersData");
            for (String player : playersData.getKeys(false)) {
                ConfigurationSection playerData = playersData.getConfigurationSection(player);
                List<ItemStack> ItemsGive = new ArrayList<>();
                if (playerData.contains("ItemsGive")) ItemsGive = loadItemsGive(playerData.getConfigurationSection("ItemsGive"));
                boolean Jail = playerData.getBoolean("Jail");
                boolean BFly = playerData.getBoolean("BFly");
                boolean AutoFeed = playerData.getBoolean("AutoFeed");
                int JailTime = playerData.getInt("JailTime");
                int BFlyTime = playerData.getInt("BFlyTime");
                int AutoFeedTime = playerData.getInt("AutoFeedTime");
                PlayerBase playerBase = new PlayerBase(player, ItemsGive, JailTime, BFlyTime, AutoFeedTime, Jail, BFly, AutoFeed);
                playersBase.put(player, playerBase);
            }
        }
    }

//    public void loadPData(String name) {
//        if (PDataConfig.contains("PlayersData")) {
//            ConfigurationSection playersData = PDataConfig.getConfigurationSection("PlayersData");
//            if (playersData.contains(name)) {
//                ConfigurationSection playerData = playersData.getConfigurationSection(name);
//                List<ItemStack> ItemsGive = new ArrayList<>();
//                if (playerData.contains("ItemsGive")) ItemsGive = loadItemsGive(playerData.getConfigurationSection("ItemsGive"));
//                boolean Jail = playerData.getBoolean("Jail");
//                boolean BFly = playerData.getBoolean("BFly");
//                boolean AutoFeed = playerData.getBoolean("AutoFeed");
//                int JailTime = playerData.getInt("JailTime");
//                int BFlyTime = playerData.getInt("BFlyTime");
//                int AutoFeedTime = playerData.getInt("AutoFeedTime");
//                PlayerBase playerBase = new PlayerBase(name, ItemsGive, JailTime, BFlyTime, AutoFeedTime, Jail, BFly, AutoFeed);
//                playersBase.put(name, playerBase);
//            }
//        }
//    }


    private List<ItemStack> loadItemsGive(ConfigurationSection playerData) {
        List<ItemStack> ItemsGive = new ArrayList<>();
        for (String config : playerData.getKeys(false)) {
            String[] item = playerData.getString(config).split(" ");
            int id = Integer.parseInt(item[0]);
            short meta = Short.parseShort(item[1]);
            int amount = Integer.parseInt(item[2]);
            ItemStack itemStack = new ItemStack(id, amount, meta);
            ItemsGive.add(itemStack);
        }

        return ItemsGive;
    }
}
