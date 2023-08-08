package com.totalcraft.soled.Listeners;

import com.Acrobot.ChestShop.Events.PreShopCreationEvent;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import static com.totalcraft.soled.Utils.ChestShopUtils.isItemCancelShop;
import static com.totalcraft.soled.Utils.PrefixMsgs.getPmTTC;
import static com.totalcraft.soled.Utils.Utils.getAdm;

public class ShopCreation implements Listener {

    @EventHandler
    public void CancelShop(PreShopCreationEvent event) {
        if (!getAdm(event.getPlayer())) {
            return;
        }
        String item = event.getSignLine((byte) 3);
        boolean cancel = false;
        if (item.startsWith("X") && item.length() >= 2 && Character.isDigit(item.charAt(1))) {
            item = item.substring(1);
        }
        String[] split = item.split(":");
        if (split.length > 1) {
            if (isItemCancelShop(split[0], split[1])) {
                cancel = true;
            }
        } else {
            if (isItemCancelShop(item, "0")) {
                cancel = true;
            }
        }
        if (cancel) {
            event.setOutcome(PreShopCreationEvent.CreationOutcome.OTHER);
            event.getSign().getWorld().dropItem(event.getSign().getLocation(), new ItemStack(Material.SIGN));
            event.getSign().getBlock().setType(Material.AIR);
            event.getPlayer().sendMessage(getPmTTC("&cVocê não pode criar loja com este item."));
        }
    }
    @EventHandler
    public void CancelBug(PreShopCreationEvent event) {
        if (!getAdm(event.getPlayer())) {
            return;
        }
        String line = event.getSignLine((byte) 2);
        String s = String.valueOf(line.charAt(0));
        if (!s.equalsIgnoreCase("S") && !s.equalsIgnoreCase("B") & !s.equalsIgnoreCase("E")) {
            event.getPlayer().sendMessage(getPmTTC("&cFormato errado de criar Loja."));
            event.setOutcome(PreShopCreationEvent.CreationOutcome.OTHER);
            event.getSign().getWorld().dropItem(event.getSign().getLocation(), new ItemStack(Material.SIGN));
            event.getSign().getBlock().setType(Material.AIR);
        }
    }
}