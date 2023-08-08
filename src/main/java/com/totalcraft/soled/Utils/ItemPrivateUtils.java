package com.totalcraft.soled.Utils;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

import static com.totalcraft.soled.Configs.ItemPrivateLog.itemsPrivate;
import static com.totalcraft.soled.Utils.PrefixMsgs.getFormatColor;
import static com.totalcraft.soled.Utils.PrefixMsgs.getPmTTC;

public class ItemPrivateUtils {
    public static List<Integer> itemsGods = Arrays.asList(4388, 4386, 4387, 4389, 4391, 4270, 4271, 4272, 4273, 4305);

    public static String getOwnerItem(ItemStack item) {
        for (int list : itemsGods) {
            if (item != null) {
                if (item.getTypeId() == list) {
                    ItemMeta itemMeta = item.getItemMeta();
                    if (itemMeta.hasLore()) {
                        for (String listLore : itemMeta.getLore()) {
                            if (listLore.contains("Privado")) {
                                String[] splitLore = listLore.split(":");
                                String loreOwner = splitLore[1];
                                String owner = ChatColor.RESET + loreOwner;
                                return item.getTypeId() + " " + owner;
                            }
                        }
                    }
                }
            }
        }
        return null;
    }

    public static void setOwnerItem(Player player, ItemStack item) {
        ItemMeta itemMeta = item.getItemMeta();
        if (itemMeta.hasLore()) {
            for (String listLore : itemMeta.getLore()) {
                if (listLore.contains("Privado")) {
                    return;
                }
            }
        }
        int num = 1;
        for (Map.Entry<String, String> entry : itemsPrivate.entrySet()) {
            String key = entry.getKey();
            if (key.contains(player.getName())) {
                String[] split1 = key.split(":");
                String[] split2 = split1[0].split("-");
                String[] split3 = split2[0].split("c");
                String[] split4 = split3[1].split("§");
                int numPrivate = Integer.parseInt(split4[0]);
                numPrivate++;
                if (num < numPrivate) {
                    num = numPrivate;
                }
            }
        }
        List<String> owner = Collections.singletonList(getFormatColor("&7Privado:&c" + num + "&7-&b " + player.getName()));
        itemMeta.setLore(owner);
        item.setItemMeta(itemMeta);
        player.sendMessage(getPmTTC("&eSetado Privado no item By &bSoledBrabo"));
    }
    public static void saveOwnerItem(Player player, ItemStack item, String invName) {
        String ownerItem = getOwnerItem(item);
        if (ownerItem != null) {
            Location loc = player.getLocation();
            TimeZone tz = TimeZone.getTimeZone("America/Sao_Paulo");
            Calendar cal = Calendar.getInstance(tz);
            Date date = cal.getTime();
            String dateFormat = date.getYear() + 1900 + "-" + (date.getMonth() + 1) + "-" + date.getDate() + " " + date.getHours() + ":" + date.getMinutes() + ":" + date.getSeconds() + " ";
            String log = player.getName() + " " + loc.getWorld().getName() + " " + loc.getBlockX() + " " + loc.getBlockY() + " " + loc.getBlockZ();
            itemsPrivate.put(ownerItem, dateFormat + log);
        }
    }
}
