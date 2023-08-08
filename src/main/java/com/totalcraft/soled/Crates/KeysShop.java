package com.totalcraft.soled.Crates;

import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

import static com.totalcraft.soled.Crates.CrateBase.*;
import static com.totalcraft.soled.Utils.PrefixMsgs.getFormatColor;
import static com.totalcraft.soled.Utils.PrefixMsgs.getPmTTC;

public class KeysShop {
    public static Inventory invKeysShop = Bukkit.createInventory(null, 54, getPmTTC("&9Keys Shop"));

    public static void createInvKeysShop() {
        invKeysShop.setItem(0, glassInfo);
        invKeysShop.setItem(1, glassInfo);
        invKeysShop.setItem(2, glassInfo);
        invKeysShop.setItem(3, glassInfo);
        invKeysShop.setItem(4, glassInfo);
        invKeysShop.setItem(5, glassInfo);
        invKeysShop.setItem(6, glassInfo);
        invKeysShop.setItem(7, glassInfo);
        invKeysShop.setItem(8, glassInfo);
        invKeysShop.setItem(9, glassInfo);
        invKeysShop.setItem(17, glassInfo);
        invKeysShop.setItem(18, glassInfo);
        invKeysShop.setItem(26, glassInfo);
        invKeysShop.setItem(27, glassInfo);
        invKeysShop.setItem(35, glassInfo);
        invKeysShop.setItem(36, glassInfo);
        invKeysShop.setItem(44, glassInfo);
        invKeysShop.setItem(45, glassInfo);
        invKeysShop.setItem(46, glassInfo);
        invKeysShop.setItem(47, glassInfo);
        invKeysShop.setItem(48, glassInfo);
        invKeysShop.setItem(49, glassInfo);
        invKeysShop.setItem(50, glassInfo);
        invKeysShop.setItem(51, glassInfo);
        invKeysShop.setItem(52, glassInfo);
        invKeysShop.setItem(53, glassInfo);
    }

    public static void setKeysInShop() {
        invKeysShop.clear();
        createInvKeysShop();
        for (ItemStack item : invHubCrate.getContents()) {
            if (item != null) {
                if (item.getItemMeta().hasLore()) {
                    String keyName = item.getItemMeta().getLore().get(1);
                    if (keyName.contains("Key:")) {
                        ItemStack newKey = item.clone();
                        ItemMeta meta = newKey.getItemMeta();
                        meta.setDisplayName(getFormatColor("&c" + keyName.split(" ")[1]));
                        List<String> lore = new ArrayList<>();
                        if (item.getItemMeta().getLore().size() > 2) {
                            lore.add("");
                            lore.add(getFormatColor(item.getItemMeta().getLore().get(2)));
                        }
                        meta.setLore(lore);
                        newKey.setItemMeta(meta);
                        invKeysShop.addItem(newKey);
                    }
                }
            };
        }
    }
}
