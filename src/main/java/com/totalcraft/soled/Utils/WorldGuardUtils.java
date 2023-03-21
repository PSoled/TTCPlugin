package com.totalcraft.soled.Utils;

import com.sk89q.worldguard.bukkit.WGBukkit;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import org.bukkit.entity.Player;

import java.util.List;

import static com.totalcraft.soled.Utils.PrefixMsgs.getPmTTC;

public class WorldGuardUtils {
    public static List<String> regionsFly;
    public static boolean CancelFlyRegion(Player player) {
        RegionManager regionManager = WGBukkit.getRegionManager(player.getWorld());
        if (regionsFly != null) {
            for (String region : regionsFly) {
                ProtectedRegion parkourRegion = regionManager.getRegion(region);
                if (parkourRegion != null && parkourRegion.contains(player.getLocation().getBlockX(), player.getLocation().getBlockY(), player.getLocation().getBlockZ())) {
                    player.setFlying(false);
                    player.sendMessage(getPmTTC("&cVocê não pode usar o fly nesta região!"));
                    return true;
                }
            }
        }
        return false;
    }
}
