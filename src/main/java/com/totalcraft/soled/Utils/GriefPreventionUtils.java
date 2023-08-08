package com.totalcraft.soled.Utils;

import com.harley.totalutilities.common.limits.BlockData;
import com.harley.totalutilities.common.limits.LimitControl;
import com.harley.totalutilities.common.limits.PlayerData;
import me.ryanhamshire.GriefPrevention.Claim;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;

import static com.totalcraft.soled.Utils.PrefixMsgs.getFormatColor;
import static com.totalcraft.soled.Utils.PrefixMsgs.getPmTTC;

public class GriefPreventionUtils {

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

    public static int getLimitsInClaim(Player player, Claim claim, boolean allClaims, boolean getMsg) {
        boolean msg = false;
        int num = 0;
        if (claim != null) {
            if (claim.getOwnerName().equalsIgnoreCase(player.getName())) {
                Location lesserBoundaryCorner = claim.getLesserBoundaryCorner();
                Location greaterBoundaryCorner = claim.getGreaterBoundaryCorner();
                int x1 = lesserBoundaryCorner.getBlockX();
                int z1 = lesserBoundaryCorner.getBlockZ();
                int x2 = greaterBoundaryCorner.getBlockX();
                int z2 = greaterBoundaryCorner.getBlockZ();
                int minX = Math.min(x1, x2);
                int minZ = Math.min(z1, z2);
                int maxX = Math.max(x1, x2);
                int maxZ = Math.max(z1, z2);
                PlayerData playerData = LimitControl.getPlayerData(claim.getOwnerName(), false);
                if (playerData != null && playerData.blocks != null) {
                    for (Object blockStr : playerData.blocks.keySet()) {
                        int limit = (int) playerData.blocks.get(blockStr);
                        if (limit > 0) {
                            String obj = (String) blockStr;
                            String[] parts = obj.split(":");
                            int blockId = Integer.parseInt(parts[0]);
                            int meta = Integer.parseInt(parts[1]);
                            for (Object test : LimitControl.blockDataMap.values()) {
                                BlockData blockData = (BlockData) test;
                                if (blockData.getBlockId() == blockId && blockData.getMetadata() == meta && claim.getOwnerName().equals(blockData.getOwner())) {
                                    if (blockData.getDimension() == 0) {
                                        if (blockData.getX() >= minX && blockData.getX() <= maxX && blockData.getZ() >= minZ && blockData.getZ() <= maxZ) {
                                            msg = true;
                                            num++;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                if (msg && allClaims && getMsg) {
                    player.sendMessage(getPmTTC("&cNo terreno &6X: &f" + (x1 + x2) / 2 + " &6Z: &f" + (z1 + z2) / 2 + " &ccontém blocos de limite e não pode ser abandonado"));
                }
                if (msg && !allClaims && getMsg) {
                    player.sendMessage(getPmTTC("&cNeste terreno contém blocos de limite e não pode ser abandonado"));
                    player.sendMessage(getPmTTC("&fVocê pode digitar &b/blocklimit &fdentro do terreno para ver os blocos de limite nele"));
                }
            }
        }
        return num;
    }

    public static void sendLimitsInClaim(Player player, Claim claim) {
        boolean msg = false;
        StringBuilder sb = new StringBuilder();
        Location lesserBoundaryCorner = claim.getLesserBoundaryCorner();
        Location greaterBoundaryCorner = claim.getGreaterBoundaryCorner();
        int x1 = lesserBoundaryCorner.getBlockX();
        int z1 = lesserBoundaryCorner.getBlockZ();
        int x2 = greaterBoundaryCorner.getBlockX();
        int z2 = greaterBoundaryCorner.getBlockZ();
        int minX = Math.min(x1, x2);
        int minZ = Math.min(z1, z2);
        int maxX = Math.max(x1, x2);
        int maxZ = Math.max(z1, z2);
        PlayerData playerData = LimitControl.getPlayerData(claim.getOwnerName(), false);
        for (Object blockStr : playerData.blocks.keySet()) {
            int limit = (int) playerData.blocks.get(blockStr);
            if (limit > 0) {
                String obj = (String) blockStr;
                String[] parts = obj.split(":");
                int blockId = Integer.parseInt(parts[0]);
                int meta = Integer.parseInt(parts[1]);
                for (Object test : LimitControl.blockDataMap.values()) {
                    BlockData blockData = (BlockData) test;
                    if (blockData.getBlockId() == blockId && blockData.getMetadata() == meta && claim.getOwnerName().equals(blockData.getOwner())) {
                        if (blockData.getDimension() == 0) {
                            if (blockData.getX() >= minX && blockData.getX() <= maxX && blockData.getZ() >= minZ && blockData.getZ() <= maxZ) {
                                msg = true;
                                sb.append("\n").append(getFormatColor("&eID: &f" + blockData.getBlockId() + "&e:&f" + blockData.getMetadata() + " &0- &6X:&f " + blockData.getX() + " &6Y:&f " + blockData.getY() + " &6Z:&f " + blockData.getZ()));
                            }
                        }
                    }
                }
            }
        }
        if (msg) {
            player.sendMessage(getPmTTC("&bBlocos com limite no seu terreno:"));
            player.sendMessage(sb.toString());
        } else {
            player.sendMessage(getPmTTC("&cNão há blocos de limite neste terreno"));
        }
    }
}





