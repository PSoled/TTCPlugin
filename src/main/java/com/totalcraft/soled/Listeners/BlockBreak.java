package com.totalcraft.soled.Listeners;

import com.totalcraft.soled.Configs.BlockProtectData;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import ru.tehkode.permissions.PermissionUser;
import ru.tehkode.permissions.bukkit.PermissionsEx;

import static com.totalcraft.soled.Configs.BlockProtectData.blockConfig;
import static com.totalcraft.soled.Utils.PrefixMsgs.getPmTTC;

public class BlockBreak implements Listener {
    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlock();
        Location blockLocation = block.getLocation();
        if (block.getLocation().getWorld().getName().equals("minerar")) {
            PermissionUser user = PermissionsEx.getUser(player);
            for (Location loc : BlockProtectData.protectedBlock.keySet()) {
                String blockProtect = BlockProtectData.protectedBlock.get(loc);
                if (blockLocation.distance(loc) <= 0 && player.getName().equals(blockProtect)) {
                    player.sendMessage(getPmTTC("&cVocê retirou um bloco protegido"));
                    blockConfig.set("protected-blocks." + loc.getWorld().getName() + "." + loc.getBlockX() + "." + loc.getBlockY() + "." + loc.getBlockZ(), null);
                    BlockProtectData.protectedBlock.remove(loc);
                    BlockProtectData.saveProtectedBlocks();
                    break;
                }
                if (blockLocation.distance(loc) <= 0 && (user.has("ttcsoled.admin") || player.isOp())) {
                    player.sendMessage(getPmTTC("&cVocê removeu um bloco protegido do player &f" + BlockProtectData.protectedBlock.get(loc)));
                    blockConfig.set("protected-blocks." + loc.getWorld().getName() + "." + loc.getBlockX() + "." + loc.getBlockY() + "." + loc.getBlockZ(), null);
                    BlockProtectData.protectedBlock.remove(loc);
                    BlockProtectData.saveProtectedBlocks();
                    break;
                }
                if (blockLocation.distance(loc) <= 3 && (user.has("ttcsoled.admin") || player.isOp())) {
                    break;
                }
                if (blockLocation.distance(loc) <= 0 && !player.getName().equals(blockProtect)) {
                    event.setCancelled(true);
                    player.sendMessage(getPmTTC("&cBloco protegido por &f" + blockProtect));
                    break;
                }
                if (blockLocation.distance(loc) <= 3 && !player.getName().equals(blockProtect)) {
                    event.setCancelled(true);
                    player.sendMessage(getPmTTC("&cHá um bloco protegido por perto"));
                    break;
                }
            }

        }
    }
}
