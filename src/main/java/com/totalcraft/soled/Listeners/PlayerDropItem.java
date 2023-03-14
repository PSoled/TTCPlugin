package com.totalcraft.soled.Listeners;

import com.totalcraft.soled.Utils.Utils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;

import static com.totalcraft.soled.Utils.BanItemUtils.getBanItem;
import static com.totalcraft.soled.Utils.PrefixMsgs.getPmTTC;

public class PlayerDropItem implements Listener {
    List<Integer> whiteList = Arrays.asList(4270, 4271, 4272, 4273, 4305, 4386, 4387, 4388, 4389, 4391, 195);
    @EventHandler
    public void PlayerDropItemEvent(PlayerDropItemEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getItemDrop().getItemStack();
        if (getBanItem(player, item)) {
            event.getItemDrop().remove();
        }
        if (whiteList.contains(item.getTypeId())) {
            if (Utils.getAdm(player)) {
                player.sendMessage(getPmTTC("&cO drop deste item é cancelado por segurança. Troque o item entre players por baú"));
                event.setCancelled(true);
            }
        }
    }
}
