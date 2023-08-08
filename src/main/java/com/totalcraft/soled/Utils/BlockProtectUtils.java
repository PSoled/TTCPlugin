package com.totalcraft.soled.Utils;

import com.totalcraft.soled.Configs.BlockProtectData;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import ru.tehkode.permissions.PermissionUser;
import ru.tehkode.permissions.bukkit.PermissionsEx;

import java.util.Iterator;

import static com.totalcraft.soled.Configs.BlockProtectData.blockConfig;
import static com.totalcraft.soled.Configs.BlockProtectData.clearProtectedBLocks;
import static com.totalcraft.soled.Utils.PrefixMsgs.getPmTTC;
import static com.totalcraft.soled.Utils.Utils.getAdm;

public class BlockProtectUtils {
    public static void clearBlocksProtect() {
        Iterator<Location> it = BlockProtectData.protectedBlock.keySet().iterator();
        while (it.hasNext()) {
            Location loc = it.next();
            Block block = loc.getWorld().getBlockAt(loc);
            if (block.getType() == Material.AIR) {
                it.remove();
            }
        }
        clearProtectedBLocks();
    }

    public static boolean blockProtectBreak(Player player, Location blockLocation) {
        PermissionUser user = PermissionsEx.getUser(player);
        Iterator<Location> it = BlockProtectData.protectedBlock.keySet().iterator();
        while (it.hasNext()) {
            Location loc = it.next();
            String owner = BlockProtectData.protectedBlock.get(loc);
            if (blockLocation.distance(loc) <= 0 && player.getName().equals(owner)) {
                player.sendMessage(getPmTTC("&cVocê retirou um bloco protegido"));
                blockConfig.set("protected-blocks." + loc.getWorld().getName() + "." + loc.getBlockX() + "." + loc.getBlockY() + "." + loc.getBlockZ(), null);
                it.remove();
                BlockProtectData.saveProtectedBlocks();
                return false;
            }
            if (blockLocation.distance(loc) <= 0 && (user.has("ttcsoled.admin") || player.isOp())) {
                player.sendMessage(getPmTTC("&cVocê removeu um bloco protegido do player &f" + BlockProtectData.protectedBlock.get(loc)));
                blockConfig.set("protected-blocks." + loc.getWorld().getName() + "." + loc.getBlockX() + "." + loc.getBlockY() + "." + loc.getBlockZ(), null);
                it.remove();
                BlockProtectData.saveProtectedBlocks();
                return false;
            }
            if (blockLocation.distance(loc) <= 3 && (user.has("ttcsoled.admin") || player.isOp())) {
                return false;
            }
            if (blockLocation.distance(loc) <= 0 && !player.getName().equals(owner)) {
                player.sendMessage(getPmTTC("&cBloco protegido por &f" + owner));
                return true;
            }
            if (blockLocation.distance(loc) <= 3 && !player.getName().equals(owner)) {
                player.sendMessage(getPmTTC("&cHá um bloco protegido por perto"));
                return true;
            }
        }
        return false;
    }

    public static boolean blockProtectPlace(Player player, Block block, Location blockLocation) {
        boolean blockCancelled = false;
        if (getAdm(player)) {
            for (Location loc : BlockProtectData.protectedBlock.keySet()) {
                String owner = BlockProtectData.protectedBlock.get(loc);
                if (block.getTypeId() == 1503 && !player.getName().equals(owner)
                        && Math.abs(blockLocation.getBlockX() - loc.getBlockX()) <= 30
                        && Math.abs(blockLocation.getBlockZ() - loc.getBlockZ()) <= 30) {
                    player.sendMessage(getPmTTC("&cHá blocos protegido por perto, Se afaste para colocar a sua Pedreira"));
                    blockCancelled = true;
                    break;
                }
                if (blockLocation.distance(loc) <= 5 && !player.getName().equals(owner)) {
                    player.sendMessage(getPmTTC("&cVocê não pode colocar blocos perto de um bloco protegido"));
                    blockCancelled = true;
                    break;
                }
            }
            if (!blockCancelled) {
                if (block.getTypeId() == 1503 || block.getTypeId() == 194 || block.getTypeId() == 195) {
                    player.sendMessage(getPmTTC("&eVocê colocou que é protegido no Mundo do Minerar"));
                    BlockProtectData.protectedBlock.put(block.getLocation(), player.getName());
                    BlockProtectData.saveProtectedBlocks();
                }
            }
        }
        return blockCancelled;
    }

    public static boolean blockProtectInteract(Player player, Location loc, ItemStack item) {
        if (getAdm(player)) {
            if (item.getTypeId() == 25284) {
                for (Location locBlock : BlockProtectData.protectedBlock.keySet()) {
                    String owner = BlockProtectData.protectedBlock.get(loc);
                    if (!player.getName().equals(owner) && loc.distance(locBlock) <= 30) {
                        player.sendMessage(getPmTTC("&cVocê não pode usar este item perto de um bloco protegido!"));
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
