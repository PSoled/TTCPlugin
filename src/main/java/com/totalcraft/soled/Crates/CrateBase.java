package com.totalcraft.soled.Crates;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.totalcraft.soled.Crates.CrateConfig.crateConfig;
import static com.totalcraft.soled.Crates.CrateConfig.crateFile;
import static com.totalcraft.soled.Crates.CrateKey.ItemKeyEvent;
import static com.totalcraft.soled.Crates.CrateKey.arrows;
import static com.totalcraft.soled.Utils.PrefixMsgs.getFormatColor;
import static com.totalcraft.soled.Utils.PrefixMsgs.getPmTTC;

public class CrateBase {
    public static Inventory invHubCrate = Bukkit.createInventory(null, 36, ChatColor.BLACK + "Hub Crate");
    public static ItemStack createKey, configKey, configItems, deleteKey, addItems, glassInfo, saveInv, changeProb, deleteItem, returnHub, returnKeys, setLore, buyKey;
    public static Map<String, Inventory> keyInventory = new HashMap<>();

    public static void setItemsCrate() {
        createKey = new ItemStack(271);
        configKey = new ItemStack(11822);
        configItems = new ItemStack(280);
        addItems = new ItemStack(388);
        glassInfo = new ItemStack(102);
        deleteKey = new ItemStack(46);
        saveInv = new ItemStack(133);
        changeProb = new ItemStack(280);
        deleteItem = new ItemStack(46);
        returnHub = new ItemStack(154);
        setLore = new ItemStack(386);
        buyKey = new ItemStack(266);
        returnKeys = new ItemStack(154);

        ItemMeta createKeyMeta = createKey.getItemMeta();
        createKeyMeta.setDisplayName(getFormatColor("&bCriar uma nova Key"));
        createKey.setItemMeta(createKeyMeta);

        ItemMeta configKeyMeta = configKey.getItemMeta();
        configKeyMeta.setDisplayName(getFormatColor("&bConfigurar esta key"));
        configKey.setItemMeta(configKeyMeta);

        ItemMeta configItemsMeta = configItems.getItemMeta();
        configItemsMeta.setDisplayName(getFormatColor("&bConfigurar os itens da key"));
        configItems.setItemMeta(configItemsMeta);

        ItemMeta addItemsMeta = addItems.getItemMeta();
        addItemsMeta.setDisplayName(getFormatColor("&bAdicionar/Remover itens nessa key"));
        addItems.setItemMeta(addItemsMeta);

        ItemMeta glassInfoMeta = glassInfo.getItemMeta();
        glassInfoMeta.setDisplayName(getPmTTC("&bCrates"));
        glassInfo.setItemMeta(glassInfoMeta);

        ItemMeta deleteKeyMeta = deleteKey.getItemMeta();
        deleteKeyMeta.setDisplayName(getFormatColor("&cDeletar esta key"));
        deleteKey.setItemMeta(deleteKeyMeta);

        ItemMeta saveInvMeta = saveInv.getItemMeta();
        saveInvMeta.setDisplayName(getFormatColor("&aSalvar modificações"));
        List<String> list = new ArrayList<>();
        list.add(getFormatColor("&b:D"));
        saveInvMeta.setLore(list);
        saveInv.setItemMeta(saveInvMeta);

        ItemMeta changeProbMeta = changeProb.getItemMeta();
        changeProbMeta.setDisplayName(getFormatColor("&bAlterar Chance do Item"));
        changeProb.setItemMeta(changeProbMeta);

        ItemMeta deleteItemMeta = deleteItem.getItemMeta();
        deleteItemMeta.setDisplayName(getFormatColor("&cRetirar este item da key"));
        deleteItem.setItemMeta(deleteItemMeta);

        ItemMeta returnHubMeta = returnHub.getItemMeta();
        returnHubMeta.setDisplayName(getFormatColor("&bVoltar Para o Hub"));
        returnHub.setItemMeta(returnHubMeta);

        ItemMeta returnKeysMeta = returnKeys.getItemMeta();
        returnKeysMeta.setDisplayName(getFormatColor("&bVoltar Para as Keys"));
        returnKeys.setItemMeta(returnKeysMeta);

        ItemMeta setLoreMeta = setLore.getItemMeta();
        setLoreMeta.setDisplayName(getFormatColor("&bColocar Lore na Key"));
        setLore.setItemMeta(setLoreMeta);

        ItemStack wool;
        wool = new ItemStack(11823, 1, (short) 0);
        ItemKeyEvent.add(wool);
        wool = new ItemStack(11823, 1, (short) 1);
        ItemKeyEvent.add(wool);
        wool = new ItemStack(11823, 1, (short) 2);
        ItemKeyEvent.add(wool);
        wool = new ItemStack(11823, 1, (short) 3);
        ItemKeyEvent.add(wool);
        wool = new ItemStack(11823, 1, (short) 4);
        ItemKeyEvent.add(wool);
        wool = new ItemStack(11823, 1, (short) 5);
        ItemKeyEvent.add(wool);
        wool = new ItemStack(11823, 1, (short) 6);
        ItemKeyEvent.add(wool);
        wool = new ItemStack(11823, 1, (short) 7);
        ItemKeyEvent.add(wool);
        wool = new ItemStack(11823, 1, (short) 8);
        ItemKeyEvent.add(wool);

        ItemStack arrow;
        arrow = new ItemStack(11824, 1, (short) 3);
        arrows.add(arrow);
        arrow = new ItemStack(11824, 1, (short) 4);
        arrows.add(arrow);
        arrow = new ItemStack(11824, 1, (short) 5);
        arrows.add(arrow);
        arrow = new ItemStack(11824, 1, (short) 6);
        arrows.add(arrow);
        arrow = new ItemStack(11824, 1, (short) 7);
        arrows.add(arrow);
        arrow = new ItemStack(11824, 1, (short) 8);
        arrows.add(arrow);
        arrow = new ItemStack(11824, 1, (short) 0);
        arrows.add(arrow);
        arrow = new ItemStack(11824, 1, (short) 1);
        arrows.add(arrow);
        arrow = new ItemStack(11824, 1, (short) 2);
        arrows.add(arrow);
    }


    public static void setHubItemsCrate() {
        invHubCrate.setItem(31, createKey);
        for (int i = 0; i < 27; i++) {
            ItemStack item = invHubCrate.getItem(i);
            if (item == null) {
                invHubCrate.setItem(i, glassInfo);
            }
        }
    }

    public static void addKeyCrate(String nameKey, String lore) {
        ItemMeta configKeyMeta = configKey.getItemMeta();
        List<String> list = new ArrayList<>();
        list.add(getFormatColor(""));
        String key = getFormatColor("&7Key:&c " + nameKey);
        list.add(key);
        if (lore != null) {
            list.add(getFormatColor(lore));
        }
        configKeyMeta.setLore(list);
        configKey.setItemMeta(configKeyMeta);
        createKeyInv(key);
        for (int i = 0; i < 27; i++) {
            ItemStack item = invHubCrate.getItem(i);
            if (item.getTypeId() == 102) {
                invHubCrate.setItem(i, configKey);
                break;
            }
        }
    }

    private static void createKeyInv(String keyName) {
        Inventory invAdd = Bukkit.createInventory(null, 54, ChatColor.BLACK + "Add Items Key");
        Inventory invConfig = Bukkit.createInventory(null, 54, ChatColor.BLACK + "Config Items Key");
        invAdd.setItem(53, saveInv);
        invConfig.setItem(53, returnHub);
        keyInventory.put(keyName + "-add", invAdd);
        keyInventory.put(keyName + "-config", invConfig);
    }

    public static void removeKeyCrate(String nameKey) {
        for (int i = 0; i < 27; i++) {
            ItemStack item = invHubCrate.getItem(i);
            if (item != null) {
                if (item.getItemMeta().hasLore()) {
                    if (item.getItemMeta().getLore().get(1).equals(nameKey)) {
                        invHubCrate.setItem(i, glassInfo);
                        String rs = nameKey.split(" ")[1];
                        crateConfig.set(rs, null);
                        try {
                            crateConfig.save(crateFile);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        return;
                    }
                }
            }
        }
    }

    public static void setItemKeyCrate(ItemStack item, String keyName) {
        Inventory inv = keyInventory.get(keyName);
        for (int i = 0; i < 54; i++) {
            ItemStack items = inv.getItem(i);
            if (items != null) {
                if (items.getTypeId() == item.getTypeId() && items.getDurability() == item.getDurability() && items.getAmount() == item.getAmount()) {
                    inv.setItem(i, item);
                    break;
                }
            }
        }
    }

    public static ItemStack giveKey(String keyName) {
        ItemStack key = new ItemStack(11822);
        ItemMeta keyMeta = key.getItemMeta();
        List<String> lore = new ArrayList<>();
        lore.add("");
        lore.add(getFormatColor("&7Key:&c " + keyName));
        keyMeta.setLore(lore);
        key.setItemMeta(keyMeta);
        return key;
    }

    public static Inventory createInvConfigKey(ItemStack key) {
        Inventory inv = Bukkit.createInventory(null, 27, ChatColor.BLACK + "Config Crate");
        inv.setItem(11, addItems);
        ItemStack newKey = key.clone();
        ItemMeta meta = newKey.getItemMeta();
        meta.setDisplayName(getFormatColor("&6Pegar a key"));
        newKey.setItemMeta(meta);
        inv.setItem(13, newKey);
        inv.setItem(15, configItems);
        inv.setItem(18, setLore);
        inv.setItem(26, deleteKey);
        for (int i = 0; i < 27; i++) {
            ItemStack item = inv.getItem(i);
            if (item == null || item.getType() == Material.AIR) {
                inv.setItem(i, glassInfo);
            }
        }
        return inv;
    }

    public static Inventory createInvConfigItem(ItemStack item) {
        Inventory inv = Bukkit.createInventory(null, 27, ChatColor.BLACK + "Config Item");
        inv.setItem(11, changeProb);
        inv.setItem(13, item);
        inv.setItem(15, deleteItem);
        for (int i = 0; i < 27; i++) {
            ItemStack glass = inv.getItem(i);
            if (glass == null) {
                inv.setItem(i, glassInfo);
            }
        }
        return inv;
    }

    public static void removeItemKey(ItemStack item, String keyName) {
        Inventory inv = keyInventory.get(keyName);
        for (int i = 0; i < 54; i++) {
            ItemStack items = inv.getItem(i);
            if (items != null) {
                if (items.getTypeId() == item.getTypeId() && items.getDurability() == item.getDurability() && items.getAmount() == item.getAmount()) {
                    inv.removeItem(items);
                    break;
                }
            }
        }
    }

    public static void addKeyConfig(String keyName, String id, String meta, String amount, String chance) {
        String config = getFormatColor("&7Key:&c " + keyName + "-config");
        String add = getFormatColor("&7Key:&c " + keyName + "-add");
        Inventory invConfig = keyInventory.get(config);
        Inventory invAdd = keyInventory.get(add);
        int ItemID = Integer.parseInt(id);
        short ItemMeta = Short.parseShort(meta);
        int ItemAmount = Integer.parseInt(amount);
        double ItemChance = Double.parseDouble(chance);

        ItemStack item = new ItemStack(ItemID, ItemAmount, ItemMeta);
        ItemMeta IMeta = item.getItemMeta();
        List<String> list = new ArrayList<>();
        list.add("");
        list.add(getFormatColor("&7Chance:&e " + ItemChance + " %"));
        IMeta.setLore(list);
        item.setItemMeta(IMeta);
        invConfig.addItem(item);
        invAdd.addItem(item);
    }

    public static String getNameRarity(double chance) {
        if (chance >= 60) {
            return getFormatColor("&7&lComum");
        }
        if (chance >= 40) {
            return getFormatColor("&9&lIncomum");
        }
        if (chance >= 20) {
            return getFormatColor("&3&lRaro");
        }
        if (chance >= 10) {
            return getFormatColor("&e&lMuito Raro");
        }
        if (chance >= 5) {
            return getFormatColor("&6&lÉpico");
        }
        if (chance < 5) {
            return getFormatColor("&5&lLendário");
        }
        return null;
    }
}

