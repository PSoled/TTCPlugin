package com.totalcraft.soled.auction;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import static com.totalcraft.soled.Utils.PrefixMsgs.getFormatColor;
import static com.totalcraft.soled.auction.AuctionBase.AuctionItems;
import static com.totalcraft.soled.auction.PlayerAuction.getItemsPlayerAuction;
import static com.totalcraft.soled.auction.PlayerAuction.playerAuction;
import static com.totalcraft.soled.auction.SetItemAuction.sellItemAuction;
import static com.totalcraft.soled.auction.SetItemAuction.openItemAuction;

public class Hub {
    public static Inventory hubAuction = Bukkit.createInventory(null, 27, ChatColor.BLACK + "Centro do Leilão");
    static ItemStack openAuction, openPlayer, openSellItem, closeInv, hubInv;

    public static void setItemsHub() {
        openAuction = new ItemStack(386);
        ItemMeta openAuctionMeta = openAuction.getItemMeta();
        openAuctionMeta.setDisplayName(getFormatColor("&bAbrir o Leilão do Servidor"));
        openAuction.setItemMeta(openAuctionMeta);

        openSellItem = new ItemStack(264);
        ItemMeta openSellItemMeta = openSellItem.getItemMeta();
        openSellItemMeta.setDisplayName(getFormatColor("&bVender um item no leilão"));
        openSellItem.setItemMeta(openSellItemMeta);

        openPlayer = new ItemStack(130);
        ItemMeta openPlayerMeta = openPlayer.getItemMeta();
        openPlayerMeta.setDisplayName(getFormatColor("&9Abrir o seu Leilão"));
        openPlayer.setItemMeta(openPlayerMeta);

        closeInv = new ItemStack(152);
        ItemMeta closeInvMeta = closeInv.getItemMeta();
        closeInvMeta.setDisplayName(getFormatColor("&cFechar o leilão"));
        closeInv.setItemMeta(closeInvMeta);

        hubInv = new ItemStack(154);
        ItemMeta hubInvMeta = hubInv.getItemMeta();
        hubInvMeta.setDisplayName(getFormatColor("&bVoltar para o HUB"));
        hubInv.setItemMeta(hubInvMeta);

        hubAuction.setItem(18, closeInv);
        hubAuction.setItem(11, openAuction);
        hubAuction.setItem(13, openSellItem);
        hubAuction.setItem(15, openPlayer);
    }

    public static void openHubs(Player player, String inv) {
        if (inv.equals(openAuction.getItemMeta().getDisplayName())) {
            player.openInventory(AuctionItems.get(0));
        }
        if (inv.equals(openSellItem.getItemMeta().getDisplayName())) {
            openItemAuction(player);
            player.openInventory(sellItemAuction.get(player.getName()));
        }
        if (inv.equals(openPlayer.getItemMeta().getDisplayName())) {
            getItemsPlayerAuction(player);
            player.openInventory(playerAuction.get(player.getName()));
        }
    }
}
