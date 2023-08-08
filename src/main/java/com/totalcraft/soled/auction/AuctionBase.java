package com.totalcraft.soled.auction;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

import static com.totalcraft.soled.Utils.PrefixMsgs.getFormatColor;
import static com.totalcraft.soled.auction.Hub.closeInv;
import static com.totalcraft.soled.auction.Hub.hubInv;

public class AuctionBase {

    public static List<Inventory> AuctionItems = new ArrayList<>();
    public static Map<String, Inventory> questionAuction = new HashMap<>();
    public static ItemStack nextAuction, previousAuction, negativeAuc, affirmativeAuc;

    public static void setItemsAuction() {
        nextAuction = new ItemStack(339);
        ItemMeta nextAuctionMeta = nextAuction.getItemMeta();
        nextAuctionMeta.setDisplayName(getFormatColor("&bPróxima página"));
        nextAuction.setItemMeta(nextAuctionMeta);

        previousAuction = new ItemStack(339);
        ItemMeta previousMeta = previousAuction.getItemMeta();
        previousMeta.setDisplayName(getFormatColor("&cPágina anterior"));
        previousAuction.setItemMeta(previousMeta);

        negativeAuc = new ItemStack(35, 1, (short) 14);
        ItemMeta negativeMeta = negativeAuc.getItemMeta();
        negativeMeta.setDisplayName(getFormatColor("&cNão comprar este item"));
        negativeAuc.setItemMeta(negativeMeta);

        affirmativeAuc = new ItemStack(35, 1, (short) 5);
        ItemMeta affirmativeMeta = affirmativeAuc.getItemMeta();
        affirmativeMeta.setDisplayName(getFormatColor("&AComprar este item"));
        affirmativeAuc.setItemMeta(affirmativeMeta);
        loadAuctionItems();
    }

    public static void addItemAuction(ItemStack item, boolean enable) {
        int value = getInventory(enable);
        AuctionItems.get(value).addItem(item);
    }

    public static void loadAuctionItems() {
        int start = 0;
        for (Inventory auction : AuctionItems) {
            if (start > 0) {
                auction.setItem(38, previousAuction);
            }
            auction.setItem(44, hubInv);
            auction.setItem(36, closeInv);
            auction.setItem(42, nextAuction);
            start++;
        }
    }
    public void addItemAuctionCfg(String name, String id, String amount, String price) {
        int idItem = Integer.parseInt(id.split(":")[0]);
        short metaItem = Short.parseShort(id.split(":")[1]);
        ItemStack item = new ItemStack(idItem, Integer.parseInt(amount), metaItem);
        ItemMeta itemMeta = item.getItemMeta();
        List<String> lore = Arrays.asList(getFormatColor("&eVendedor:&b " + name), getFormatColor("&eValor: &aR$ " + price));
        itemMeta.setLore(lore);
        item.setItemMeta(itemMeta);
        addItemAuction(item, true);
    }

    private static int countEmptySlots(Inventory inventory) {
        int emptySlots = 0;
        for (int i = 0; i < inventory.getSize(); i++) {
            if (inventory.getItem(i) == null || slotEmpty(inventory.getItem(i))) {
                emptySlots++;
            }
        }
        return emptySlots;
    }

    private static boolean slotEmpty(ItemStack item) {
        if (item != null) {
            if (item.getItemMeta().hasDisplayName()) {
                String name = item.getItemMeta().getDisplayName();
                if (name.equals(closeInv.getItemMeta().getDisplayName())) {
                    return true;
                }
                if (name.equals(hubInv.getItemMeta().getDisplayName())) {
                    return true;
                }
                if (name.equals(nextAuction.getItemMeta().getDisplayName())) {
                    return true;
                }
                return name.equals(previousAuction.getItemMeta().getDisplayName());
            }
        }
        return false;
    }

    public static int getInventory(boolean enable) {
        for (Inventory auction : AuctionItems) {
            if (countEmptySlots(auction) > 9) {
                String[] part = auction.getName().split(" ");
                return Integer.parseInt(part[1]) - 1;
            }
        }
        int value = AuctionItems.size() + 1;
        Inventory newInv = Bukkit.createInventory(null, 45, ChatColor.AQUA + "Leilão " + value);
        AuctionItems.add(newInv);
        if (enable) {
            return value - 1;
        }
        return value;
    }

    public static void removeItemAuction(ItemStack item) {
        for (Inventory inv : AuctionItems) {
            for (ItemStack itemStack : inv) {
                if (itemStack != null) {
                    if (itemStack.getTypeId() == item.getTypeId() && itemStack.getDurability() == item.getDurability()) {
                        if (itemStack.getAmount() == item.getAmount()) {
                            if (item.getItemMeta().hasLore()) {
                                if (itemStack.getItemMeta().getLore().get(1).equals(item.getItemMeta().getLore().get(1))) {
                                    inv.removeItem(itemStack);
                                    organizeInv();
                                    return;
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public static void organizeInv() {
        for (Inventory inv : AuctionItems) {
            inv.removeItem(closeInv);
            inv.removeItem(hubInv);
            if (inv.getName().contains("1")) inv.removeItem(nextAuction);
            else {
                inv.removeItem(nextAuction);
                inv.removeItem(previousAuction);
            }
            ItemStack[] contents = inv.getContents();
            ItemStack[] organizedContents = new ItemStack[contents.length];
            int nextEmptySlot = 0;
            for (ItemStack itemStack : contents) {
                if (itemStack != null) {
                    organizedContents[nextEmptySlot] = itemStack;
                    nextEmptySlot++;
                }
            }
            inv.setContents(organizedContents);
            if (inv.getName().contains("1")) inv.setItem(42, nextAuction);
            else {
                inv.setItem(38, previousAuction);
                inv.setItem(42, nextAuction);
            }
            inv.setItem(36, closeInv);
            inv.setItem(40, hubInv);
        }
    }

    public static void questionAuction(Player player, ItemStack item) {
        questionAuction.put(player.getName(), Bukkit.createInventory(null, 27, ChatColor.BLACK + "Item: " + ChatColor.GOLD + item.getType().name()));
        questionAuction.get(player.getName()).setItem(13, item);
        questionAuction.get(player.getName()).setItem(11, negativeAuc);
        questionAuction.get(player.getName()).setItem(15, affirmativeAuc);
        questionAuction.get(player.getName()).setItem(26, hubInv);
        player.openInventory(questionAuction.get(player.getName()));
    }

    public static boolean containsItem(ItemStack item) {
        for (Inventory inv : AuctionItems) {
            if (inv.contains(item)) {
                return true;
            }
        }
        return false;
    }

    public static boolean containsItem(String name, String id, String amount, String price) {
        int idItem = Integer.parseInt(id.split(":")[0]);
        short metaItem = Short.parseShort(id.split(":")[1]);
        ItemStack item = new ItemStack(idItem, Integer.parseInt(amount), metaItem);
        ItemMeta itemMeta = item.getItemMeta();
        List<String> lore = Arrays.asList(getFormatColor("&eVendedor:&b " + name), getFormatColor("&eValor: &aR$ " + price));
        itemMeta.setLore(lore);
        item.setItemMeta(itemMeta);
        for (Inventory inv : AuctionItems) {
            if (inv.contains(item)) {
                return true;
            }
        }
        return false;
    }
}

