package com.totalcraft.soled.Listeners;

import com.totalcraft.soled.Utils.BlockProtectUtils;
import com.totalcraft.soled.Utils.CollectBlocksUtils;
import com.totalcraft.soled.Utils.ItemPrivateUtils;
import me.ryanhamshire.GriefPrevention.Claim;
import me.ryanhamshire.GriefPrevention.GriefPrevention;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import ru.tehkode.permissions.PermissionUser;
import ru.tehkode.permissions.bukkit.PermissionsEx;

import java.util.HashMap;
import java.util.Map;

import static com.totalcraft.soled.Utils.BanItemUtils.getBanItem;
import static com.totalcraft.soled.Utils.ClearChunkUtils.chunkInClaim;
import static com.totalcraft.soled.Utils.ClearChunkUtils.clearChunkVerify;
import static com.totalcraft.soled.Utils.GriefPreventionUtils.hasPermClaim;
import static com.totalcraft.soled.Utils.ItemPrivateUtils.itemsGods;
import static com.totalcraft.soled.Utils.ItemPrivateUtils.saveOwnerItem;
import static com.totalcraft.soled.Utils.PrefixMsgs.getPmTTC;

public class PlayerInteract implements Listener {

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Location loc = player.getLocation();
        ItemStack item = player.getItemInHand();
        if (!player.getWorld().getName().equals("world") && !player.getWorld().getName().equals("spawn")) {
            if (event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK) {
                CollectBlocksUtils.collectBlockInteract(player);
            }
        }
        if (loc.getWorld().getName().equals("minerar")) {
            event.setCancelled(BlockProtectUtils.blockProtectInteract(player, loc, item));
        }
        if (getBanItem(player, item)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void itemPrivateEvent(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack hand = player.getItemInHand();
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_AIR && event.getPlayer().isSneaking()) {
            if (itemsGods.contains(hand.getTypeId())) {
                ItemPrivateUtils.setOwnerItem(player, hand);
                saveOwnerItem(player, hand, "hand");
            }
        }
    }

    public static Map<String, Chunk> listClearChunk = new HashMap<>();

    @EventHandler
    public void itemClearChunk(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = player.getItemInHand();
        if (item.getTypeId() == 27192) {
            if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                Claim claim = GriefPrevention.instance.dataStore.getClaimAt(player.getLocation(), false, null);
                if (claim == null) {
                    player.sendMessage(getPmTTC("&cVocê não está em um terreno"));
                    return;
                }
                if (!claim.getOwnerName().equalsIgnoreCase(player.getName())) {
                    player.sendMessage(getPmTTC("&cVocê não é dono deste terreno"));
                    return;
                }
                if (chunkInClaim(player)) {
                    player.sendMessage(getPmTTC("&cEsta chunk não está totalmente dentro do seu terreno"));
                    return;
                }
                player.sendMessage("\n" + getPmTTC("&aVocê selecionou a chunk que você está para limpar ela"));
                player.sendMessage(getPmTTC("&fUse &b/clearchunk confirm &fpara confirmar a limpeza da chunk"));
                player.sendMessage(getPmTTC("&fUse &b/clearchunk cancel &fpara cancelar a limpeza da chunk"));
                listClearChunk.put(player.getName(), player.getLocation().getChunk());
                clearChunkVerify(player, player.getLocation().getChunk());
            }
        }
    }

    @EventHandler
    public void giveKeyNStart(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = player.getItemInHand();
        if (item.getTypeId() == 27208) {
            if (event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_AIR) {
                player.setItemInHand(null);
                Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "keys give " + player.getName() + " &dNetherStar " + item.getAmount());
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void CancelInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        PermissionUser user = PermissionsEx.getUser(player);
        if (user.has("ttcsoled.bypass.grief") || player.isOp()) {
            return;
        }
        Block block = event.getClickedBlock();
        if (block != null) {
            if (block.getState() instanceof Sign || block.getTypeId() == 3050 || block.getTypeId() == 145) {
                return;
            }
        }
        ItemStack item = event.getItem();
        if (item != null) {
            if (item.getTypeId() == 7493 || (item.getTypeId() == 4373 && item.getDurability() == 1)) {
                return;
            }
        }
        Claim claim = GriefPrevention.instance.dataStore.getClaimAt(player.getLocation(), false, null);
        if (claim != null) {
            if (!claim.getOwnerName().equals(player.getName())) {
                if (hasPermClaim(player, claim)) {
                    event.setCancelled(true);
                }
            }
        }
    }
}
