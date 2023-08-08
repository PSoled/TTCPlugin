package com.totalcraft.soled.auction;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import ru.tehkode.permissions.PermissionUser;
import ru.tehkode.permissions.bukkit.PermissionsEx;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.totalcraft.soled.Utils.PrefixMsgs.getFormatColor;
import static com.totalcraft.soled.auction.AuctionBase.AuctionItems;
import static com.totalcraft.soled.auction.Hub.closeInv;
import static com.totalcraft.soled.auction.Hub.hubInv;
import static com.totalcraft.soled.auction.SetItemAuction.listRankLimit;

public class PlayerAuction {
    public static Map<String, Inventory> playerAuction = new HashMap<>();
    public static ItemStack negativePlayerAuc, affirmativePlayerAuc, changePrice;

    public static void setItemsPlayerAuc() {
        negativePlayerAuc = new ItemStack(35, 1, (short) 14);
        ItemMeta negativeMeta = negativePlayerAuc.getItemMeta();
        negativeMeta.setDisplayName(getFormatColor("&cNão quero tirar o item do leilão"));
        negativePlayerAuc.setItemMeta(negativeMeta);

        affirmativePlayerAuc = new ItemStack(35, 1, (short) 5);
        ItemMeta affirmativeMeta = affirmativePlayerAuc.getItemMeta();
        affirmativeMeta.setDisplayName(getFormatColor("&aRetirar este item do leilão"));
        affirmativePlayerAuc.setItemMeta(affirmativeMeta);

        changePrice = new ItemStack(288);
        ItemMeta changePriceMeta = changePrice.getItemMeta();
        changePriceMeta.setDisplayName(getFormatColor("&bAlterar valor do item"));
        changePrice.setItemMeta(changePriceMeta);
    }
    public static void getItemsPlayerAuction(Player player) {
        playerAuction.put(player.getName(), Bukkit.createInventory(null, 36, ChatColor.BLACK + "Administrar o seu leilão"));
        playerAuction.get(player.getName()).setItem(27, closeInv);
        playerAuction.get(player.getName()).setItem(35, hubInv);
        for (Inventory auction : AuctionItems) {
            for (ItemStack item : auction) {
                if (item != null) {
                    if (item.getItemMeta().hasLore()) {
                        String name = item.getItemMeta().getLore().get(0).split(" ")[1];
                        if (player.getName().equalsIgnoreCase(name)) {
                            playerAuction.get(player.getName()).addItem(item);
                        }
                    }
                }
            }
        }

        int limitPlayer = 5;
        PermissionUser user = PermissionsEx.getUser(player);
        for (Map.Entry<String, Integer> entry : listRankLimit().entrySet()) {
            if (user.has(entry.getKey())) {
                limitPlayer = entry.getValue();
                break;
            }
        }
        ItemStack info = getItemInfo(limitPlayer);
        for (int i = limitPlayer; i < 27; i++) {
            playerAuction.get(player.getName()).setItem(i, info);
        }
    }
    private static ItemStack getItemInfo(int limit) {
        ItemStack itemInfo = new ItemStack(102);
        ItemMeta meta = itemInfo.getItemMeta();
        List<String> list = new ArrayList<>();
        list.add(getFormatColor("&bSeu limite de itens no leilão é &c" + limit));
        meta.setDisplayName(getFormatColor("&e&lInformação:"));
        meta.setLore(list);
        itemInfo.setItemMeta(meta);
        return itemInfo;
    }
    public static void questionRemoveItem(Player player, ItemStack item) {
        if (item != null) {
            if (item.getItemMeta().hasLore()) {
                playerAuction.get(player.getName()).clear();
                playerAuction.get(player.getName()).setItem(13, item);
                playerAuction.get(player.getName()).setItem(11, negativePlayerAuc);
                playerAuction.get(player.getName()).setItem(15, affirmativePlayerAuc);
                playerAuction.get(player.getName()).setItem(31, changePrice);
                playerAuction.get(player.getName()).setItem(35, hubInv);
            }
        }
    }
}
