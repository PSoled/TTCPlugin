package com.totalcraft.soled.Utils;

import com.totalcraft.soled.Configs.MainConfig;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.Random;

public class EventoMinaUtils {


    public static void placeRandomBlocks(List<String> listBlock) {
        World world = Bukkit.getWorld(MainConfig.worldLocatinaMina);
        int x1 = MainConfig.x1Reset;
        int y1 = MainConfig.y1Reset;
        int z1 = MainConfig.z1Reset;
        int x2 = MainConfig.x2Reset;
        int y2 = MainConfig.y2Reset;
        int z2 = MainConfig.z2Reset;
        Location loc1 = new Location(world, x1, y1, z1);
        Location loc2 = new Location(world, x2, y2, z2);
        int minX = Math.min(x1, x2);
        int maxX = Math.max(x1, x2);
        int minY = Math.min(y1, y2);
        int maxY = Math.max(y1, y2);
        int minZ = Math.min(z1, z2);
        int maxZ = Math.max(z1, z2);

        Random random = new Random();
        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                for (int z = minZ; z <= maxZ; z++) {
                    Location loc = new Location(world, x, y, z);
                    if (loc1.distance(loc) <= loc1.distance(loc2)) {
                        int totalChance = 0;
                        for (String block : listBlock) {
                            String[] blockData = block.split(":");
                            totalChance += Integer.parseInt(blockData[2]);
                        }
                        int randomChance = random.nextInt(totalChance) + 1;
                        Material material;
                        for (String block : listBlock) {
                            String[] blockData = block.split(":");
                            randomChance -= Integer.parseInt(blockData[2]);
                            if (randomChance <= 0) {
                                int id = Integer.parseInt(blockData[0]);
                                byte data = Byte.parseByte(blockData[1]);
                                material = Material.getMaterial(id);
                                if (material != null) {
                                    loc.getBlock().setType(material);
                                    loc.getBlock().setData(data);
                                }
                                break;
                            }
                        }
                    }
                }
            }
        }
    }

    public static ItemStack createPickaxe(int Efficieny, int Fortune, int Unbreaking) {
        ItemStack pickaxe = new ItemStack(Material.DIAMOND_PICKAXE);
        ItemMeta pickaxeMeta = pickaxe.getItemMeta();

        pickaxeMeta.addEnchant(Enchantment.DIG_SPEED, Efficieny, true);
        pickaxeMeta.addEnchant(Enchantment.LOOT_BONUS_BLOCKS, Fortune, true);
        pickaxeMeta.addEnchant(Enchantment.DURABILITY, Unbreaking, true);

        pickaxe.setItemMeta(pickaxeMeta);
        return pickaxe;
    }
}

