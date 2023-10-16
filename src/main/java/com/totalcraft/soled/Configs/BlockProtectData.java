package com.totalcraft.soled.Configs;

import com.totalcraft.soled.Main;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class BlockProtectData {
    private final Main main;

    public BlockProtectData(Main main) {
        this.main = main;
    }

    public static File blockFile;
    public static YamlConfiguration blockConfig;
    public static HashMap<Location, String> protectedBlock = new HashMap<>();

    public static void saveProtectedBlocks() {
        for (Location loc : protectedBlock.keySet()) {
            blockConfig.set("protected-blocks." + loc.getWorld().getName() + "." + loc.getBlockX() + "." + loc.getBlockY() + "." + loc.getBlockZ(), protectedBlock.get(loc));
        }

        try {
            blockConfig.save(blockFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void clearProtectedBLocks() {
        ConfigurationSection blocksSection = blockConfig.getConfigurationSection("protected-blocks");
        for (String worldName : blocksSection.getKeys(false)) {
            ConfigurationSection worldSection = blocksSection.getConfigurationSection(worldName);
            for (String xStr : worldSection.getKeys(false)) {
                ConfigurationSection xSection = worldSection.getConfigurationSection(xStr);
                for (String yStr : xSection.getKeys(false)) {
                    ConfigurationSection ySection = xSection.getConfigurationSection(yStr);
                    for (String zStr : ySection.getKeys(false)) {
                        ConfigurationSection zSection = ySection.getConfigurationSection(zStr);
                        int x = Integer.parseInt(xStr);
                        int y = Integer.parseInt(yStr);
                        int z = Integer.parseInt(zStr);
                        World world = Bukkit.getWorld(worldName);
                        if (world.getBlockAt(x, y, z).getType() == Material.AIR) {
                            ySection.set(zStr, null);
                        }
                        if (zSection != null) {
                            String playerName = zSection.getString(zStr);
                            if (playerName == null || playerName.isEmpty()) {
                                zSection.getParent().set(zStr, null);
                            }
                        }
                    }
                    if (ySection.getKeys(false).isEmpty()) {
                        ySection.getParent().set(yStr, null);
                    }
                }
                if (xSection.getKeys(false).isEmpty()) {
                    xSection.getParent().set(xStr, null);
                }
            }
            if (worldSection.getKeys(false).isEmpty()) {
                worldSection.getParent().set(worldName, null);
            }
        }
        try {
            blockConfig.save(blockFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadProtectedBlocks() {
        blockFile = new File(main.getDataFolder(), "data/blockprotect.yml");
        blockConfig = YamlConfiguration.loadConfiguration(blockFile);
        if (!blockFile.exists()) {
            blockConfig.set("protected-blocks.", "start");
            try {
                blockConfig.save(blockFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        ConfigurationSection protectedBlocksSection = blockConfig.getConfigurationSection("protected-blocks");
        try {
            for (String worldName : protectedBlocksSection.getKeys(false)) {
                World world = Bukkit.getWorld(worldName);
                ConfigurationSection worldSection = protectedBlocksSection.getConfigurationSection(worldName);
                for (String xStr : worldSection.getKeys(false)) {
                    for (String yStr : worldSection.getConfigurationSection(xStr).getKeys(false)) {
                        for (String zStr : worldSection.getConfigurationSection(xStr + "." + yStr).getKeys(false)) {
                            int x = Integer.parseInt(xStr);
                            int y = Integer.parseInt(yStr);
                            int z = Integer.parseInt(zStr);
                            Location loc = new Location(world, x, y, z);
                            String playerName = (worldSection.getString(xStr + "." + yStr + "." + zStr));
                            protectedBlock.put(loc, playerName);
                        }
                    }
                }
            }
        } catch (Exception a) {
            System.out.println("O arquivo blockprotect.yml não está criado corretamente" + a.getMessage());
            a.printStackTrace();
        }
    }

    public static String getLocBlock(String name) {
        StringBuilder result = new StringBuilder();
        for (Location loc : protectedBlock.keySet()) {
            String comp = protectedBlock.get(loc);
            if (comp.equals(name)) {
                result.append("X: ").append(loc.getBlockX()).append(" Y: ").append(loc.getBlockY()).append(" Z: ").append(loc.getBlockZ()).append("\n");
            }
        }
        return (result.length() == 0) ? "null" : result.toString();
    }
}
