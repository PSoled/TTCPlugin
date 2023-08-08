package com.totalcraft.soled.Utils;

import me.ryanhamshire.GriefPrevention.Claim;
import me.ryanhamshire.GriefPrevention.GriefPrevention;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.InventoryHolder;

import java.util.HashMap;
import java.util.Map;

import static com.totalcraft.soled.Configs.MainConfig.blockLimit;
import static com.totalcraft.soled.Utils.PrefixMsgs.getPmTTC;

public class ClearChunkUtils {
    public static boolean blockLimitVerify(Block block) {
        int idBlock = block.getTypeId();
        int metaBlock = block.getData();
        for (String blocklist : blockLimit) {
            String[] value = blocklist.split(":");
            int id = Integer.parseInt(value[0]);
            int meta = Integer.parseInt(value[1]);
            if (idBlock == id && metaBlock == meta) {
                return true;
            }
        }
        return false;
    }

    public static boolean blockContainsInventory(Block block) {
        return (block.getState() instanceof InventoryHolder);
    }

    public static boolean chunkInClaim(Player player) {
        Chunk chunk = player.getLocation().getChunk();
        int chunkX = chunk.getX() << 4;
        int chunkZ = chunk.getZ() << 4;
        for (int x = chunkX; x < chunkX + 16; x++) {
            for (int z = chunkZ; z < chunkZ + 16; z++) {
                Location loc = new Location(player.getWorld(), x, 10, z);
                Claim chunkInClaim = GriefPrevention.instance.dataStore.getClaimAt(loc, false, null);
                if (chunkInClaim == null) {
                    return true;
                }
            }
        }
        return false;
    }

    public static void clearChunk(Player player, Chunk chunk) {
        int startX = chunk.getX() << 4;
        int startZ = chunk.getZ() << 4;
        for (int x = startX; x < startX + 16; x++) {
            for (int z = startZ; z < startZ + 16; z++) {
                for (int y = 0; y < 256; y++) {
                    Block block = player.getWorld().getBlockAt(x, y, z);
                    if (block.getTypeId() != 7 && block.getType() != Material.AIR) {
                        if (!blockLimitVerify(block)) {
                            if (!blockContainsInventory(block)) {
                                block.setType(Material.AIR);
                            }
                        }
                    }
                }
            }
        }
    }

    public static Map<String, Integer> blocksChunk = new HashMap<>();
    public static void clearChunkVerify(Player player, Chunk chunk) {
        boolean limit = false, inventory = false, msg = false;
        int blocks = 0;
        int startX = chunk.getX() << 4;
        int startZ = chunk.getZ() << 4;
        for (int x = startX; x < startX + 16; x++) {
            for (int z = startZ; z < startZ + 16; z++) {
                for (int y = 0; y < 256; y++) {
                    Block block = player.getWorld().getBlockAt(x, y, z);
                    if (block.getTypeId() != 7 && block.getType() != Material.AIR) {
                        blocks++;
                    }
                    if (blockLimitVerify(block)) {
                        limit = true;
                        msg = true;
                    }
                    if (blockContainsInventory(block)) {
                        inventory = true;
                    }
                }
            }
        }
        blocksChunk.put(player.getName(), blocks);
        if (msg || blocks < 100) {
            player.sendMessage("\n" + getPmTTC("&cAviso está chunk contém:"));
        }
        if (limit) {
            player.sendMessage("\n" + getPmTTC("&cblocos de limite."));
        }
        if (inventory) {
            player.sendMessage("\n" + getPmTTC("&cblocos com inventário.\n&9Obs: Chest dentro de dungeons podem ter sido verificado"));
        }
        if (blocks < 100) {
            player.sendMessage("\n" + getPmTTC("&cMenos de 100 blocos para limpar, TEM CERTEZA DISSO MEU CHEFE???"));
        }
        if (msg) {
            player.sendMessage("\n" + getPmTTC("&eVocê pode limpar a chunk normalmente, mas todos blocos citados serão ignorados na limpeza"));
        }
    }
}
