package com.totalcraft.soled.Utils;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

import static com.totalcraft.soled.Configs.BanItemCfg.banItemList;
import static com.totalcraft.soled.Utils.PrefixMsgs.getPmTTC;

public class BanItemUtils {
    public static List<ItemStack> getItemBanList() {
        List<ItemStack> itemStackList = new ArrayList<>();
        for (String item : banItemList) {
            String[] value = item.split(":");
            int id = Integer.parseInt(value[0]);
            int meta = Integer.parseInt(value[1]);
            itemStackList.add(new ItemStack(Material.getMaterial(id), 64, (short) meta));
        }
        return itemStackList;
    }

    public static boolean getBanItem(Player player) {
        List<ItemStack> banList = getItemBanList();
        if (Utils.getAdm(player)) {
            for (int i = 0; i < player.getInventory().getSize(); i++) {
                ItemStack inventoryItem = player.getInventory().getItem(i);
                if (inventoryItem == null) {
                    continue;
                }
                for (ItemStack banned : banList) {
                    if (banned.isSimilar(inventoryItem)) {
                        player.getInventory().remove(inventoryItem);
                        player.sendMessage(getPmTTC("&c&lItem ") + banned.getTypeId() + ":" + banned.getDurability() + " Banido");
                        return true;
                    }
                }
            }
        }
        return false;
    }
    public static boolean getBanItem(Player player, ItemStack item) {
        List<ItemStack> banList = getItemBanList();
        if (Utils.getAdm(player)) {
            for (ItemStack banned : banList) {
                if (banned.isSimilar(item)) {
                    player.getInventory().remove(item);
                    player.sendMessage(getPmTTC("&c&lItem ") + banned.getTypeId() + ":" + banned.getDurability() + " Banido");
                    return true;
                }
            }
        }
        return false;
    }
}
