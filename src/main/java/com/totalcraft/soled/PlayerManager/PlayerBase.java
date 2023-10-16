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
    private final List<ItemStack> ItemsGive;
    private int JailTime, BFlyTime, AutoFeedTime;

    public boolean Jail, BFly, AutoFeed;
    private Map<String, Location> homes;
    private String cacheHomeName, SaveGod;
    private Location cacheHomeLoc;

    public PlayerBase(String name) {
        this.name = name;
        this.playerPT = PlayerPT.getPlayerPT(name);
        this.ItemsGive = new ArrayList<>();
        this.JailTime = 0;
        this.BFlyTime = 0;
        this.AutoFeedTime = 0;
        this.homes = new HashMap<>();
        this.SaveGod = "null";
    }

    public PlayerBase(String name, List<ItemStack> ItemsGive, int JailTime, int BFlyTime, int AutoFeedTime, boolean Jail, boolean BFly, boolean AutoFeed, String SaveGod) {
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
        this.SaveGod = SaveGod;
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

    public void addItemsGive(int id, short meta, int amount) {
        ItemStack item = new ItemStack(id, amount, meta);
        if (amount > 64) {
            item = new ItemStack(id, 64, meta);
            int packs = amount / 64;

            for (int i = 0; i < packs; i++) {
                this.ItemsGive.add(item);
            }
            int rest = amount % 64;
            if (rest > 0) {
                item = new ItemStack(id, rest, meta);
                this.ItemsGive.add(item);
            }
            return;
        }
        this.ItemsGive.add(item);
        this.saveData();
    }

    public int getBFlyTime() {
        return BFlyTime;
    }

    public void setBFlyTime(int BFlyTime) {
        this.BFlyTime = BFlyTime;
        this.saveData();
    }

    public int getJailTime() {
        return JailTime;
    }

    public void setJailTime(int jailTime) {
        JailTime = jailTime;
        this.saveData();
    }

    public int getAutoFeedTime() {
        return AutoFeedTime;
    }

    public void setAutoFeedTime(int autoFeedTime) {
        AutoFeedTime = autoFeedTime;
        this.saveData();
    }

    public Map<String, Location> getHomes() {
        return this.homes;
    }

    public void addHome(String name, Location loc) {
        this.homes.put(name, loc);
    }

    public void removeHome(String name) {
        this.homes.remove(name);
        this.saveData();
    }

    public void setHomes(Map<String, Location> homes) {
        this.homes = homes;
        this.saveData();
    }

    public String getCacheHomeName() {
        return cacheHomeName;
    }

    public Location getCacheHomeLoc() {
        return cacheHomeLoc;
    }

    public void setCacheHomeName(String cacheHomeName) {
        this.cacheHomeName = cacheHomeName;
        this.saveData();
    }

    public void setCacheHomeLoc(Location cacheHomeLoc) {
        this.cacheHomeLoc = cacheHomeLoc;
        this.saveData();
    }

    public void setSaveGod(String saveGod) {
        SaveGod = saveGod;
        this.saveData();
    }

    public String getSaveGod() {
        return SaveGod;
    }
}
