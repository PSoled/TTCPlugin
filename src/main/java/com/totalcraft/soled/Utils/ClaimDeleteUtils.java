package com.totalcraft.soled.Utils;

import me.ryanhamshire.GriefPrevention.Claim;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;


import java.util.ArrayList;
import java.util.List;

import static com.totalcraft.soled.Configs.MainConfig.blockLimit;
import static com.totalcraft.soled.Utils.PrefixMsgs.getFormatColor;
import static com.totalcraft.soled.Utils.PrefixMsgs.getPmTTC;

public class ClaimDeleteUtils {
    public static boolean claimContainsBlock(Player player, Claim claim, World world, boolean allClaims) {
        boolean hasBlock = false;

        List<String> stringList = new ArrayList<>();
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
                for (int x = minX; x <= maxX; x++) {
                    for (int y = minY; y <= y2; y++) {
                        for (int z = minZ; z <= maxZ; z++) {
                            Block block = world.getBlockAt(x, y, z);
                            for (String blocklist : blockLimit) {
                                String[] value = blocklist.split(":");
                                int id = Integer.parseInt(value[0]);
                                int meta = Integer.parseInt(value[1]);
                                if (block.getTypeId() == id && block.getData() == meta) {
                                    stringList.add(getFormatColor("&eID: &f" + id + "&e:&f" + meta + " &0-" + " &6X: &f" + x + " &6Y: &f" + y + " &6Z: &f" + z));
                                    hasBlock = true;
                                }
                            }
                        }
                    }
                }
                if (hasBlock) {
                    String message = allClaims ? "&cNo terreno &6X: &f" + (x1 + x2) / 2 + " &6Z: &f" + (z1 + z2) / 2 + " &ccontém blocos de limite nestas localizações e não pode ser abandonado" : "&cNeste terreno contém blocos de limite nestas localizações e não pode ser abandonado";
                    player.sendMessage(getPmTTC(message) + "\n ");
                    for (String list : stringList) {
                        player.sendMessage(list);
                    }
                    player.sendMessage(" ");
                }
            }
        }
        return hasBlock;
    }
}

