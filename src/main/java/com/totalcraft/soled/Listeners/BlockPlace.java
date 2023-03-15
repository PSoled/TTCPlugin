package com.totalcraft.soled.Listeners;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.totalcraft.soled.Commands.BlockProtect;
import com.totalcraft.soled.Configs.BlockProtectData;
import com.totalcraft.soled.Configs.MainConfig;
import com.totalcraft.soled.Utils.BlockProtectUtils;
import com.totalcraft.soled.Utils.Utils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.craftbukkit.v1_5_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_5_R3.block.CraftBlock;
import org.bukkit.craftbukkit.v1_5_R3.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import ru.tehkode.permissions.PermissionUser;
import ru.tehkode.permissions.bukkit.PermissionsEx;

import java.util.Arrays;
import java.util.List;

import static com.totalcraft.soled.Configs.BlockProtectData.blockConfig;
import static com.totalcraft.soled.Utils.PrefixMsgs.getPmTTC;

public class BlockPlace implements Listener {
    List<Integer> blockMinerar = Arrays.asList(2007, 1510, 2003, 54, 146, 975, 251, 61, 250, 1023, 194, 195, 1503);

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlockPlaced();
        Location blockLocation = block.getLocation();
        if (player.getWorld().getName().equals("minerar")) {
            event.setCancelled(BlockProtectUtils.blockProtectPlace(player, block, blockLocation));
        }
        if (Utils.getAdm(player)) {
            if (player.getWorld().getName().equals("minerar")) {
                if (block.getState() instanceof InventoryHolder) {
                    if (!blockMinerar.contains(block.getTypeId())) {
                        player.sendMessage(getPmTTC("&cEste bloco não pode ser colocado no mundo do minerar"));
                        event.setCancelled(true);
                    }
                }
            }
        }
    }
}

