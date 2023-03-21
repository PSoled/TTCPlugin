package com.totalcraft.soled.Utils;

import me.ryanhamshire.GriefPrevention.Claim;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.HashMap;

import static com.totalcraft.soled.Configs.MainConfig.blockLimit;
import static com.totalcraft.soled.Utils.PrefixMsgs.getFormatColor;
import static com.totalcraft.soled.Utils.PrefixMsgs.getPmTTC;

public class GriefPreventionUtils {
    private final Plugin plugin;

    public GriefPreventionUtils(Plugin plugin) {
        this.plugin = plugin;
    }

    static String info = "&bDigite /blocklimit dentro do Terreno para saber a localização dos blocos limitados";
    public static HashMap<String, Boolean> hasBlock = new HashMap<>();
    public static HashMap<String, Integer> getDifferenceBlocks = new HashMap<>();

    public int claimContainsBlock(Player player, Claim claim, World world, boolean allClaims, boolean getList, boolean getMsg) {
        int numDivisions = 0;
        hasBlock.put(player.getName(), false);
        getDifferenceBlocks.put(player.getName(), 0);
        if (claim != null) {
            if (claim.getOwnerName().equalsIgnoreCase(player.getName())) {
                Location lesserBoundaryCorner = claim.getLesserBoundaryCorner();
                Location greaterBoundaryCorner = claim.getGreaterBoundaryCorner();
                int x1 = lesserBoundaryCorner.getBlockX();
                int z1 = lesserBoundaryCorner.getBlockZ();
                int x2 = greaterBoundaryCorner.getBlockX();
                int y2 = 255;
                int z2 = greaterBoundaryCorner.getBlockZ();
                int minX = Math.min(x1, x2);
                int minY = 0;
                int minZ = Math.min(z1, z2);
                int maxX = Math.max(x1, x2);
                int maxZ = Math.max(z1, z2);
                numDivisions = (int) Math.round((maxX - minX) * (maxZ - minZ) / 1000.0);
                if (numDivisions == 0) {
                    numDivisions = 1;
                }
                int divisionSize = (maxX - minX + 1) / numDivisions;
                for (int i = 0; i < numDivisions; i++) {
                    int start = minX + i * divisionSize;
                    int end = (i == numDivisions - 1) ? maxX : (start + divisionSize - 1);
                    Bukkit.getScheduler().runTaskLater(plugin, () -> {
                        for (int x = start; x <= end; x++) {
                            for (int y = minY; y <= y2; y++) {
                                for (int z = minZ; z <= maxZ; z++) {
                                    Block block = world.getBlockAt(x, y, z);
                                    if (block.getType() != Material.AIR && block.getType() != Material.STONE && block.getType() != Material.DIRT
                                            && block.getType() != Material.SAND && block.getType() != Material.GRAVEL
                                            && !(block.getType() == Material.SNOW || block.getType() == Material.SNOW_BLOCK)) {
                                        for (String blocklist : blockLimit) {
                                            String[] value = blocklist.split(":");
                                            int id = Integer.parseInt(value[0]);
                                            int meta = Integer.parseInt(value[1]);
                                            if (block.getTypeId() == id && block.getData() == meta) {
                                                hasBlock.put(player.getName(), true);
                                                if (getList) {
                                                    player.sendMessage(getFormatColor("&eID: &f" + id + "&e:&f" + meta + " &0-" + " &6X: &f" + x + " &6Y: &f" + y + " &6Z: &f" + z));
                                                }
                                                getDifferenceBlocks.put(player.getName(), getDifferenceBlocks.get(player.getName()) + 1);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }, i * 20L);
                }
                Bukkit.getScheduler().runTaskLater(plugin, () -> {
                    if (hasBlock.get(player.getName()) && getMsg) {
                        String message = allClaims ? "&cNo terreno &6X: &f" + (x1 + x2) / 2 + " &6Z: &f" + (z1 + z2) / 2 + " &ccontém blocos de limite e não pode ser abandonado\n" + info : "" +
                                "&cNeste terreno contém blocos de limite e não pode ser abandonado\n" + info;
                        player.sendMessage(getPmTTC(message));
                    }
                },9L * numDivisions);
            }
        }
        return numDivisions;
    }

    public static boolean hasPermEntry(Player player, Claim claim) {
        ArrayList<String> allPerms = new ArrayList<>();
        claim.getPermissions(allPerms, allPerms, allPerms, allPerms);
        return !allPerms.contains(player.getName()) && !allPerms.contains(player.getName().toLowerCase()) && !allPerms.contains("public");
    }

    public static boolean hasPermClaim(Player player, Claim claim) {
        ArrayList<String> allPerms = new ArrayList<>();
        ArrayList<String> screwThis = new ArrayList<>();
        claim.getPermissions(allPerms, allPerms, screwThis, allPerms);
        return !allPerms.contains(player.getName()) && !allPerms.contains(player.getName().toLowerCase());
    }
}




