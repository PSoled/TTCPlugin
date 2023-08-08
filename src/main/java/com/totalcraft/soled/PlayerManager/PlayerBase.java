package com.totalcraft.soled.PlayerManager;

import com.totalcraft.soled.PlayTIme.PlayerPT;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.totalcraft.soled.PlayerManager.DataSave.SavePData;

public class PlayerBase {
    private final String name;
    private final PlayerPT playerPT;
    private List<ItemStack> ItemsGive;
    private int JailTime, BFlyTime, AutoFeedTime;
    public boolean Jail, BFly, AutoFeed;
    private Map<String, Location> homes;
    private String cacheHomeName;
    private Location cacheHomeLoc;

    public PlayerBase(String name) {
        this.name = name;
        this.playerPT = PlayerPT.getPlayerPT(name);
        this.ItemsGive = new ArrayList<>();
        this.JailTime = 0;
        this.BFlyTime = 0;
        this.AutoFeedTime = 0;
        this.homes = new HashMap<>();

    }

    public PlayerBase(String name, List<ItemStack> ItemsGive, int JailTime, int BFlyTime, int AutoFeedTime, boolean Jail, boolean BFly, boolean AutoFeed) {
        this.name = name;
        this.playerPT = PlayerPT.getPlayerPT(name);
        this.ItemsGive = ItemsGive;
        this.JailTime = JailTime;
        this.BFlyTime = BFlyTime;
        this.AutoFeedTime = AutoFeedTime;
        this.Jail = Jail;
        this.BFly = BFly;
        this.AutoFeed = AutoFeed;
        this.homes = new HashMap<>();
    }

    public static Map<String, PlayerBase> playersBase = new HashMap<>();

    public static PlayerBase getPlayerBase(String name) {
        OfflinePlayer player = Bukkit.getOfflinePlayer(name);
        if (!player.hasPlayedBefore()) return null;
        for (String playerBase : playersBase.keySet()) {
            if (name.equalsIgnoreCase(playerBase)) {
                name = playerBase;
            }
        }
        if (!playersBase.containsKey(name)) playersBase.put(name, new PlayerBase(name));
        PlayerBase playerBase = playersBase.get(name);
        playerBase.saveData();
        return playerBase;
    }

    public void saveData() {
        SavePData(this);
    }

    public String getName() {
        return name;
    }

    public PlayerPT getPlayerPT() {
        return playerPT;
    }

    public List<ItemStack> getItemsGive() {
        return ItemsGive;
    }

    public void addItemsGive(ItemStack itemsGive) {
        this.ItemsGive.add(itemsGive);
    }

    public void setItemsGive(List<ItemStack> itemsGive) {
        ItemsGive = itemsGive;
    }

    public int getBFlyTime() {
        return BFlyTime;
    }

    public void setBFlyTime(int BFlyTime) {
        this.BFlyTime = BFlyTime;
    }

    public int getJailTime() {
        return JailTime;
    }

    public void setJailTime(int jailTime) {
        JailTime = jailTime;
    }

    public int getAutoFeedTime() {
        return AutoFeedTime;
    }

    public void setAutoFeedTime(int autoFeedTime) {
        AutoFeedTime = autoFeedTime;
    }

    public Map<String, Location> getHomes() {
        return this.homes;
    }

    public void addHome(String name, Location loc) {
        this.homes.put(name, loc);
    }

    public void removeHome(String name) {
        this.homes.remove(name);
    }

    public void setHomes(Map<String, Location> homes) {
        this.homes = homes;
    }

    public String getCacheHomeName() {
        return cacheHomeName;
    }

    public Location getCacheHomeLoc() {
        return cacheHomeLoc;
    }

    public void setCacheHomeName(String cacheHomeName) {
        this.cacheHomeName = cacheHomeName;
    }

    public void setCacheHomeLoc(Location cacheHomeLoc) {
        this.cacheHomeLoc = cacheHomeLoc;
    }
}
