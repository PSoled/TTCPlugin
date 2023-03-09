package com.totalcraft.soled.Listeners;

import com.totalcraft.soled.Commands.BlockProtect;
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
        if (player.getWorld().getName().equals("minerar")) {
            event.setCancelled(BlockProtect.blockProtectPlace(player, block, blockLocation));
        }
    }
}
