package com.totalcraft.soled.auction;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import ru.tehkode.permissions.PermissionUser;
import ru.tehkode.permissions.bukkit.PermissionsEx;

import java.util.HashMap;
import java.util.Map;

import static com.totalcraft.soled.Utils.PrefixMsgs.getFormatColor;
import static com.totalcraft.soled.auction.AuctionBase.AuctionItems;
import static com.totalcraft.soled.auction.ConfigAuction.priceMinServer;
import static com.totalcraft.soled.auction.Hub.*;

public class SetItemAuction {
    public static Map<String, Inventory> sellItemAuction = new HashMap<>();
    public static ItemStack infoItem, affirmativeHub, negativeHub;

    public static void setItemsSetItem() {
        infoItem = new ItemStack(399);
        ItemMeta infoItemMeta = infoItem.getItemMeta();
        infoItemMeta.setDisplayName(getFormatColor("&bSelecione o item no seu inventário"));
        infoItem.setItemMeta(infoItemMeta);

        affirmativeHub = new ItemStack(35, 1, (short) 5);
        ItemMeta affirmativeMeta = affirmativeHub.getItemMeta();
        affirmativeMeta.setDisplayName(getFormatColor("&aLeiloar este item"));
        affirmativeHub.setItemMeta(affirmativeMeta);

        negativeHub = new ItemStack(35, 1, (short) 14);
        ItemMeta negativeMeta = negativeHub.getItemMeta();
        negativeMeta.setDisplayName(getFormatColor("&cNão quero leiloar este item"));
        negativeHub.setItemMeta(negativeMeta);
    }

    public static void openItemAuction(Player player) {
        sellItemAuction.put(player.getName(), Bukkit.createInventory(null, 27, ChatColor.AQUA + "Venda: " + ChatColor.BLACK + player.getName()));
        sellItemAuction.get(player.getName()).setItem(13, infoItem);
        sellItemAuction.get(player.getName()).setItem(18, closeInv);
        sellItemAuction.get(player.getName()).setItem(26, hubInv);
    }

    public static void setItemAuction(Player player, ItemStack item) {
        sellItemAuction.get(player.getName()).clear();
        if (itemSell(item)) {
            return;
        }
        sellItemAuction.get(player.getName()).setItem(13, item);
        sellItemAuction.get(player.getName()).setItem(11, negativeHub);
        sellItemAuction.get(player.getName()).setItem(15, affirmativeHub);
        sellItemAuction.get(player.getName()).setItem(26, hubInv);
    }

    public static boolean itemSell(ItemStack item) {
        if (item.getItemMeta().hasDisplayName()) {
            String itemName = item.getItemMeta().getDisplayName();
            return itemName.equals(negativeHub.getItemMeta().getDisplayName()) || itemName.equals(affirmativeHub.getItemMeta().getDisplayName()) || itemName.equals(infoItem.getItemMeta().getDisplayName());
        }
        return false;
    }

    public static boolean limitSetItem(Player player) {
        int itemsInAuction = 0;
        int limitPlayer = 5;
        for (Inventory auction : AuctionItems) {
            for (ItemStack item : auction) {
                if (item != null) {
                    if (item.getItemMeta().hasLore()) {
                        String name = item.getItemMeta().getLore().get(0).split(" ")[1];
                        if (player.getName().equalsIgnoreCase(name)) {
                            itemsInAuction++;
                        }
                    }
                }
            }
        }
        PermissionUser user = PermissionsEx.getUser(player);
        for (Map.Entry<String, Integer> entry : listRankLimit().entrySet()) {
            if (user.has(entry.getKey())) {
                limitPlayer = entry.getValue();
                break;
            }
        }
        return itemsInAuction >= limitPlayer;
    }

    public static Map<String, Integer> listRankLimit() {
        Map<String, Integer> listLimit = new HashMap<>();
        listLimit.put("auction.total", 12);
        listLimit.put("auction.dragon", 10);
        listLimit.put("auction.quantum", 9);
        listLimit.put("auction.ultimate", 8);
        listLimit.put("auction.hybrid", 7);
        listLimit.put("auction.advanced", 6);
        return listLimit;
    }

    public static boolean getPriceMin(ItemStack item, long price, long amount) {
        String id = item.getTypeId() + ":" + item.getDurability();
        String idMeta = item.getTypeId() + ":*";
        if (priceMinServer.containsKey(id)) {
            return price < priceMinServer.get(id) * amount;
        }
        if (priceMinServer.containsKey(idMeta)) {
            return price < priceMinServer.get(idMeta) * amount;
        }
        return false;
    }
}

