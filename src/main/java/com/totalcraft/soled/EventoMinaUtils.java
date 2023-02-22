package com.totalcraft.soled;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Random;

public class EventoMinaUtils {


    public static void placeRandomBlocks() {
        World world = Bukkit.getWorld(Configs.worldLocatinaMina);
        int x1 = Configs.x1Reset;
        int y1 = Configs.y1Reset;
        int z1 = Configs.z1Reset;
        int x2 = Configs.x2Reset;
        int y2 = Configs.y2Reset;
        int z2 = Configs.z2Reset;
        Location loc1 = new Location(world, x1, y1, z1);
        Location loc2 = new Location(world, x2, y2, z2);
        int minX = Math.min(x1, x2);
        int maxX = Math.max(x1, x2);
        int minY = Math.min(y1, y2);
        int maxY = Math.max(y1, y2);
        int minZ = Math.min(z1, z2);
        int maxZ = Math.max(z1, z2);

// Colocar blocos aleatórios na área definida
        Random random = new Random();
        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                for (int z = minZ; z <= maxZ; z++) {
                    Location loc = new Location(world, x, y, z);
                    if (loc1.distance(loc) <= loc1.distance(loc2)) {
                        // Escolher um bloco aleatório com base em suas porcentagens de chance
                        int totalChance = 0;
                        for (String block : Configs.blocks) {
                            String[] blockData = block.split(":");
                            totalChance += Integer.parseInt(blockData[2]);
                        }
                        int randomChance = random.nextInt(totalChance) + 1;
                        Material material;
                        for (String block : Configs.blocks) {
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

