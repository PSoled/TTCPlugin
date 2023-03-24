package com.totalcraft.soled.Configs;

import com.totalcraft.soled.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.File;
import java.io.IOException;
import java.util.*;

import static com.totalcraft.soled.Commands.FilterBlock.filterChest;
import static com.totalcraft.soled.Utils.PrefixMsgs.getFormatColor;

public class FilterBlockData {
    private final Main main;
    public FilterBlockData(Main main) {
        this.main = main;
    }
    public static File filterFile;
    public static YamlConfiguration filterConfig;
    public static ItemStack redBlock = new ItemStack(35, 1, (short) 14);
    public static ItemStack greenBlock = new ItemStack(35, 1, (short) 5);

    public static void saveFilterBlock() {
        for (Map.Entry<String, Inventory> entry : filterChest.entrySet()) {
            String playerName = entry.getKey();
            Inventory inventory = entry.getValue();
            List<String> itemIds = new ArrayList<>();

            for (ItemStack item : inventory.getContents()) {
                if (item != null && item.getType() != Material.AIR) {
                    String itemId = String.valueOf(item.getTypeId());
                    itemId += ":" + item.getDurability();
                    itemIds.add(itemId);
                }
            }
            filterConfig.set(playerName, itemIds);
        }

        try {
            filterConfig.save(filterFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadFilterBlock() {
        filterFile = new File(main.getDataFolder(), "data/filterblock.yml");
        filterConfig = YamlConfiguration.loadConfiguration(filterFile);
        filterChest = new HashMap<>();
        ItemMeta redMeta = redBlock.getItemMeta();
        ItemMeta greenMeta = greenBlock.getItemMeta();
        redMeta.setDisplayName(getFormatColor("&bFiltro de bloco &cDesativado"));
        greenMeta.setDisplayName(getFormatColor("&bFiltro de bloco &aAtivado"));
        redBlock.setItemMeta(redMeta);
        greenBlock.setItemMeta(greenMeta);

        ConfigurationSection section = filterConfig.getConfigurationSection("");
        if (section != null) {
            for (String playerName : section.getKeys(false)) {
                List<String> itemIds = filterConfig.getStringList(playerName);
                Inventory inventory = Bukkit.createInventory(null, 18, ChatColor.WHITE + "Filtro: " + ChatColor.BLACK + playerName);
                inventory.setItem(17, redBlock);

                for (String itemId : itemIds) {
                    String[] parts = itemId.split(":");
                    int id = Integer.parseInt(parts[0]);
                    int meta = parts.length > 1 ? Integer.parseInt(parts[1]) : 0;
                    if (id == 35 && meta == 14) {
                        inventory.setItem(17, redBlock);
                    } else if (id == 35 && meta == 5) {
                        inventory.setItem(17, greenBlock);
                    } else {
                        ItemStack item = new ItemStack(id, 1, (short) meta);
                        inventory.addItem(item);
                    }
                }

                filterChest.put(playerName, inventory);
            }
        }
    }
}
