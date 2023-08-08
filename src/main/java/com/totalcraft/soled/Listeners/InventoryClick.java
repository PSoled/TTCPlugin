package com.totalcraft.soled.Listeners;

import com.totalcraft.soled.Commands.MinValues;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import static com.totalcraft.soled.Commands.FilterBlock.filterChest;
import static com.totalcraft.soled.Commands.MinValues.nextPage;
import static com.totalcraft.soled.Commands.MinValues.previousPage;
import static com.totalcraft.soled.Configs.FilterBlockData.greenBlock;
import static com.totalcraft.soled.Configs.FilterBlockData.redBlock;
import static com.totalcraft.soled.Utils.BanItemUtils.getBanItem;
import static com.totalcraft.soled.Utils.ItemPrivateUtils.saveOwnerItem;

public class InventoryClick implements Listener {

    @EventHandler
    public void banItemEvent(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        Inventory inv = event.getInventory();
        if (inv != null) {
            ItemStack item = event.getCurrentItem();
            if (item != null && getBanItem(player, item)) {
                event.setCancelled(true);
                inv.removeItem(item);
            }
        }
    }

    @EventHandler
    public void itemPrivate(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        ItemStack item = event.getCurrentItem();
        saveOwnerItem(player, item, event.getInventory().getName());
    }

    @EventHandler
    public void cancelDup(InventoryClickEvent event) {
        Inventory inv = event.getInventory();

        if (inv != null) {
            if (inv.getName().equals("Hopper")) {
                if (event.getHotbarButton() >= 0 && event.getHotbarButton() <= 8) {
                    event.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void MinValueEvent(InventoryClickEvent event) {
        String invName = ChatColor.BLACK + "Valores Mínimos";
        if (event.getInventory().getName().contains(invName)) {
            event.setCancelled(true);
            ItemStack item = event.getCurrentItem();
            if (item != null) {
                if (item.getItemMeta() != null) {
                    if (item.getItemMeta().hasDisplayName()) {
                        Player player = (Player) event.getWhoClicked();
                        String itemName = item.getItemMeta().getDisplayName();
                        int index = Integer.parseInt(event.getInventory().getName().split(" ")[2]);
                        if (itemName.contains(nextPage.getItemMeta().getDisplayName())) {
                            if (index >= 0 && index < MinValues.minValues.size()) {
                                player.openInventory(MinValues.minValues.get(index));
                            }
                        }
                        if (itemName.contains(previousPage.getItemMeta().getDisplayName())) {
                            player.openInventory(MinValues.minValues.get(index - 2));
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void filterBlocKEvent(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        Inventory inv = event.getInventory();
        Inventory invFilter = filterChest.get(player.getName());
        if (inv != null && invFilter != null) {
            if (invFilter.getName().equals(inv.getName())) {
                ItemStack item = event.getCurrentItem();
                if (item != null) {
                    event.setCancelled(true);
                    if (item.getTypeId() == 35 && item.getDurability() == 14) {
                        invFilter.setItem(17, greenBlock);
                        return;
                    } else if (item.getTypeId() == 35 && item.getDurability() == 5) {
                        invFilter.setItem(17, redBlock);
                        return;
                    }
                    ItemStack newItem = new ItemStack(item.getTypeId(), 1, item.getDurability());
                    if (invFilter.contains(newItem)) {
                        invFilter.remove(newItem);
                        return;
                    }
                    int count = 0;
                    for (ItemStack itemStack : invFilter.getContents()) {
                        if (itemStack == null) {
                            invFilter.setItem(count, newItem);
                            break;
                        }
                        count++;
                    }
                }
            }
        }
    }
}