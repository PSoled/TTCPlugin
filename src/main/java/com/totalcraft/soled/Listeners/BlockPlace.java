package com.totalcraft.soled.Listeners;

import com.sk89q.jnbt.NBTInputStream;
import com.totalcraft.soled.Utils.BlockProtectUtils;
import com.totalcraft.soled.Utils.Utils;
import me.dpohvar.powernbt.nbt.NBTBase;
import me.dpohvar.powernbt.nbt.NBTContainerBlock;
import me.dpohvar.powernbt.nbt.NBTTagCompound;
import me.dpohvar.powernbt.nbt.NBTTagString;
import net.minecraft.server.v1_5_R3.TileEntity;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.craftbukkit.v1_5_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_5_R3.block.CraftBlock;
import org.bukkit.craftbukkit.v1_5_R3.block.CraftBlockState;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static com.totalcraft.soled.Commands.FilterBlock.filterChest;
import static com.totalcraft.soled.Utils.PrefixMsgs.getPmTTC;

public class BlockPlace implements Listener {

    List<Integer> blockMinerar = Arrays.asList(2007, 1510, 2003, 54, 146, 975, 251, 61, 250, 1023, 194, 195, 1503, 2516, 2513);

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) throws InstantiationException, IllegalAccessException {
        Player player = event.getPlayer();
        Block block = event.getBlockPlaced();
        Location blockLocation = block.getLocation();
        ItemStack itemHand = player.getItemInHand();
        if (player.getWorld().getName().equals("minerar")) {
            if (BlockProtectUtils.blockProtectPlace(player, block, blockLocation)) {
                event.setCancelled(true);
            }
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

