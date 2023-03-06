package com.totalcraft.soled.Listeners;

import com.totalcraft.soled.Configs.BlockProtectData;
import com.totalcraft.soled.Configs.MainConfig;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import ru.tehkode.permissions.PermissionUser;
import ru.tehkode.permissions.bukkit.PermissionsEx;

import static com.totalcraft.soled.Configs.BlockProtectData.blockConfig;
import static com.totalcraft.soled.Utils.PrefixMsgs.getPmTTC;

public class BlockPlace implements Listener {
    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlockPlaced();
        Location blockLocation = block.getLocation();
        if (block.getLocation().getWorld().getName().equals("minerar")) {
            boolean blockCancelled = false;
            PermissionUser user = PermissionsEx.getUser(player);
            if (!user.has("ttcsoled.admin") && !player.isOp()) {
                for (Location loc : BlockProtectData.protectedBlock.keySet()) {
                    String blockProtect = BlockProtectData.protectedBlock.get(loc);
                    if (block.getTypeId() == 1503 && blockLocation.distance(loc) <= 30 && !player.getName().equals(blockProtect)) {
                        event.setCancelled(true);
                        player.sendMessage(getPmTTC("&cHá blocos protegido por perto, Se afaste para colocar a sua Pedreira"));
                        blockCancelled = true;
                        break;
                    }
                    if (blockLocation.distance(loc) <= 5 && !player.getName().equals(blockProtect)) {
                        event.setCancelled(true);
                        player.sendMessage(getPmTTC("&cVocê pode colocar blocos perto de um bloco protegido"));
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
        }
    }
}
