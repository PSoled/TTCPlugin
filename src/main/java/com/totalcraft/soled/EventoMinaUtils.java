package com.totalcraft.soled;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import java.util.Random;

public class EventoMinaUtils {
    public void placeRandomBlocks() {
        // Definir a área onde os blocos serão colocados
        World world = Bukkit.getWorld("world");
        int x1 = 20; // coordenada X do primeiro canto da área
        int y1 = 80; // coordenada Y do primeiro canto da área
        int z1 = 20; // coordenada Z do primeiro canto da área
        int x2 = -20; // coordenada X do segundo canto da área
        int y2 = 120; // coordenada Y do segundo canto da área
        int z2 = -20; // coordenada Z do segundo canto da área
        Location loc1 = new Location(world, x1, y1, z1);
        Location loc2 = new Location(world, x2, y2, z2);
        int minX = Math.min(x1, x2);
        int maxX = Math.max(x1, x2);
        int minY = Math.min(y1, y2);
        int maxY = Math.max(y1, y2);
        int minZ = Math.min(z1, z2);
        int maxZ = Math.max(z1, z2);

        // Definir os blocos e suas porcentagens de chance
        int[][] blocks = {
                {41, 15}, // 15% de chance de ser bloco 41
                {42, 15}, // 15% de chance de ser bloco 42
                {152, 8}, // 8% de chance de ser bloco 152
                {57, 9}, // 9% de chance de ser bloco 57
        };

        // Colocar blocos aleatórios na área definida
        Random random = new Random();
        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                for (int z = minZ; z <= maxZ; z++) {
                    Location loc = new Location(world, x, y, z);
                    if (loc1.distance(loc) <= loc1.distance(loc2)) {
                        // Escolher um bloco aleatório com base em suas porcentagens de chance
                        int totalChance = 0;
                        for (int i = 0; i < blocks.length; i++) {
                            totalChance += blocks[i][1];
                        }
                        int randomChance = random.nextInt(totalChance) + 1;
                        int blockId = 0;
                        for (int i = 0; i < blocks.length; i++) {
                            randomChance -= blocks[i][1];
                            if (randomChance <= 0) {
                                blockId = blocks[i][0];
                                break;
                            }
                        }
                        Material material = Material.getMaterial(blockId);
                        if (material != null) {
                            loc.getBlock().setType(material);
                        }
                    }
                }
            }
        }
    }
}