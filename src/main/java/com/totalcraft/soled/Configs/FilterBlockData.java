package com.totalcraft.soled.Configs;

import com.totalcraft.soled.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.totalcraft.soled.Commands.FilterBlock.filterBoolean;
import static com.totalcraft.soled.Commands.FilterBlock.filterChest;

public class FilterBlockData {
    private final Main main;
    public FilterBlockData(Main main) {
        this.main = main;
    }
    public static File filterFile;
    public static YamlConfiguration filterConfig;

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
        filterBoolean = new HashMap<>();

        ConfigurationSection section = filterConfig.getConfigurationSection("");
        if (section != null) {
            for (String playerName : section.getKeys(false)) {
                List<String> itemIds = filterConfig.getStringList(playerName);
                Inventory inventory = Bukkit.createInventory(null, 9, ChatColor.WHITE + "Filtro: " + ChatColor.BLACK + playerName);

                for (String itemId : itemIds) {
                    String[] parts = itemId.split(":");
                    int id = Integer.parseInt(parts[0]);
                    int meta = parts.length > 1 ? Integer.parseInt(parts[1]) : 0;
                    ItemStack item = new ItemStack(id, 1, (short) meta);
                    inventory.addItem(item);
                }

                filterChest.put(playerName, inventory);
            }
        }
    }
}
