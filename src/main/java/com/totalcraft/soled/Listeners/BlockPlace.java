package com.totalcraft.soled.Listeners;

import com.harley.totalutilities.common.limits.BlockData;
import com.harley.totalutilities.common.limits.LimitControl;
import com.harley.totalutilities.common.limits.PlayerData;
import com.totalcraft.soled.Utils.BlockProtectUtils;
import com.totalcraft.soled.Utils.Utils;
import me.ryanhamshire.GriefPrevention.Claim;
import me.ryanhamshire.GriefPrevention.GriefPrevention;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;


import static com.harley.totalutilities.common.limits.LimitControl.blockDataMap;
import static com.totalcraft.soled.Utils.GriefPreventionUtils.hasPermClaim;
import static com.totalcraft.soled.Utils.PrefixMsgs.getPmTTC;
import static com.totalcraft.soled.Utils.Utils.getAdm;

public class BlockPlace implements Listener {

    List<Integer> blockMinerar = Arrays.asList(2007, 1510, 2003, 54, 146, 975, 251, 61, 250, 1023, 194, 195, 1503, 2516, 2513);

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlockPlaced();
        Location blockLocation = block.getLocation();
        ItemStack itemHand = player.getItemInHand();
        if (player.getWorld().getName().equals("minerar")) {
            if (BlockProtectUtils.blockProtectPlace(player, block, blockLocation)) {
                event.setCancelled(true);
            }
        }

        if (getAdm(player)) {
            if (player.getWorld().getName().equals("minerar")) {
                if (block.getState() instanceof InventoryHolder) {
                    if (!blockMinerar.contains(block.getTypeId())) {
                        player.sendMessage(getPmTTC("&cEste bloco não pode ser colocado no mundo do minerar"));
                        event.setCancelled(true);
                    }
                }
            }
        }

//        if (player.getName().equals("PlayerSoled")) {
//            String blockStr = block.getTypeId() + ":" + block.getData();
//            Claim claim = GriefPrevention.instance.dataStore.getClaimAt(block.getLocation(), false, null);
//            if (claim != null) {
//                if (LimitControl.limitedBlockNames.containsKey(blockStr)) {
//                    if (!claim.getOwnerName().equals(player.getName())) {
//                        if (!hasPermClaim(player, claim)) {
//                            PlayerData playerData = LimitControl.getPlayerData(claim.getOwnerName().toLowerCase(), false);
//                            boolean canPlace = playerData.hasReachedLimit(blockStr);
//                            if (canPlace) {
//                                player.sendMessage(getPmTTC("&cO dono do terreno já está com limite deste bloco no máximo."));
//                            } else {
//                                player.sendMessage(getPmTTC("&f&lVocê colocou &b&l" + (String)LimitControl.limitedBlockNames.get(blockStr) + " &f&lno limite de &b&l" + claim.getOwnerName() + " &f&lVocê ainda pode colocar &b&l" + playerData.getRemaining(blockStr)));
//                                BlockData blockData = new BlockData(block.getTypeId(), block.getData(), block.getX(), block.getY(), block.getZ(), 0, claim.getOwnerName().toLowerCase());
//                                LimitControl.addBlock(playerData, blockData);
//                                playerData.addBlock(blockStr);
//                                player.sendMessage(getPmTTC("&f&lVocê colocou &b&l" + (String)LimitControl.limitedBlockNames.get(blockStr) + " &f&lno limite de &b&l" + claim.getOwnerName() + " &f&lVocê ainda pode colocar &b&l" + playerData.getRemaining(blockStr)));
//                                LimitControl.saveData();
//                            }
//                        }
//                    }
//                }
//            }
//        }
    }
}

