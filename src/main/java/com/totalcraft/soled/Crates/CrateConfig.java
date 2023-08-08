package com.totalcraft.soled.Crates;

import com.totalcraft.soled.Main;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import static com.totalcraft.soled.Crates.CrateBase.*;
import static com.totalcraft.soled.Crates.KeysShop.createInvKeysShop;
import static com.totalcraft.soled.Crates.KeysShop.setKeysInShop;

public class CrateConfig {
    private final Main main;

    public CrateConfig(Main main) {
        this.main = main;
    }

    public static File crateFile;
    public static YamlConfiguration crateConfig;

    public void loadCrate() {
        setItemsCrate();
        setHubItemsCrate();
        loadData();
        loadItems();
        createInvKeysShop();
        setKeysInShop();
    }

    private void loadData() {
        crateFile = new File(main.getDataFolder(), "data/crate.yml");
        crateConfig = YamlConfiguration.loadConfiguration(crateFile);
    }

    private void loadItems() {
        for (String keyConfig : crateConfig.getKeys(false)) {
            String lore = null;
            String key = keyConfig;
            if (keyConfig.contains("-")) {
                String[] split = keyConfig.split("-");
                key = split[0];
                lore = split[1];
            }
            addKeyCrate(key, lore);
            String[] partsItems = crateConfig.getString(keyConfig).split("-");
            for (String items : partsItems) {
                String[] itemsPart = items.split(" ");
                if (itemsPart.length > 3) {
                    String id = itemsPart[0];
                    String meta = itemsPart[1];
                    String amount = itemsPart[2];
                    String chance = itemsPart[3];
                    addKeyConfig(key, id, meta, amount, chance);
                }
            }
        }
    }

    public static void saveItemsCrate() {
        for (Map.Entry<String, Inventory> entry : keyInventory.entrySet()) {
            String keyName = entry.getKey().split(" ")[1].split("-")[0];
            StringBuilder config = new StringBuilder(keyName);
            StringBuilder sb = new StringBuilder();
            for (ItemStack items : entry.getValue()) {
                if (items != null) {
                    if (items.getItemMeta().hasLore()) {
                        if (items.getItemMeta().hasDisplayName()) {
                            if (!items.getItemMeta().getDisplayName().equals(saveInv.getItemMeta().getDisplayName())) {
                                String lore = items.getItemMeta().getLore().get(1);
                                String name = lore.split(" ")[1];
                                sb.append(items.getTypeId()).append(" ").append(items.getDurability()).append(" ").append(items.getAmount()).append(" ").append(name).append("-");
                            }
                        } else {
                            String lore = items.getItemMeta().getLore().get(1);
                            String name = lore.split(" ")[1];
                            sb.append(items.getTypeId()).append(" ").append(items.getDurability()).append(" ").append(items.getAmount()).append(" ").append(name).append("-");
                        }
                    }
                }
            }
            for (ItemStack item : invHubCrate.getContents()) {
                if (item != null) {
                    if (item.getItemMeta().hasLore()) {
                        if (item.getItemMeta().getLore().size() > 2) {
                            if (item.getItemMeta().getLore().get(1).contains(config)) {
                                config.append("-").append(item.getItemMeta().getLore().get(2));
                            }
                        }
                    }
                }
            }
            for (String keyConfig : crateConfig.getKeys(false)) {
                if (keyConfig.contains(keyName)) {
                    crateConfig.set(keyConfig, null);
                }
            }
            crateConfig.set(config.toString(), sb.toString());
        }
        try {
            crateConfig.save(crateFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
