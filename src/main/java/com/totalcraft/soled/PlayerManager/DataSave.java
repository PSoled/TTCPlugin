package com.totalcraft.soled.PlayerManager;

import org.bukkit.inventory.ItemStack;

import java.io.IOException;

import static com.totalcraft.soled.PlayerManager.MainPData.PDataConfig;
import static com.totalcraft.soled.PlayerManager.MainPData.PDataFile;
import static com.totalcraft.soled.PlayerManager.PlayerBase.playersBase;

public class DataSave {
    public static void SavePDataBase() {
        for (PlayerBase player : playersBase.values()) {
            SavePData(player);
        }
    }
    public static void SavePData(PlayerBase player) {
        int num = 1;
        String path = "PlayersData." + player.getName() + ".";
        PDataConfig.set(path + "ItemsGive", null);
        for (ItemStack item : player.getItemsGive()) {
            PDataConfig.set(path + "ItemsGive." + num, item.getTypeId() + " " + item.getDurability() + " " + item.getAmount());
            num++;
        }
        PDataConfig.set(path + "Jail", player.Jail);
        PDataConfig.set(path + "JailTime", player.getJailTime());
        PDataConfig.set(path + "BFly", player.BFly);
        PDataConfig.set(path + "BFlyTime", player.getBFlyTime());
        PDataConfig.set(path + "AutoFeed", player.AutoFeed);
        PDataConfig.set(path + "AutoFeedTime", player.getAutoFeedTime());

        try {
            PDataConfig.save(PDataFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}