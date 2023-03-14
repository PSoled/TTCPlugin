package com.totalcraft.soled.Utils;

import com.totalcraft.soled.Commands.RandomTp;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.security.SecureRandom;

import static com.totalcraft.soled.Utils.PrefixMsgs.getPmTTC;

public class RandomTpUtils {
    private static final int MAX_RANGE = 10000 * 2;
    private static final SecureRandom random = new SecureRandom();

    public static Location getRandomTp(World world, Player player) {
        int x, y, z;
        x = getRandomCoordinate();
        z = getRandomCoordinate();
        y = random.nextInt(200);
        Block block = world.getBlockAt(x, y, z);
        if (block.getType() != Material.AIR) {
            y = world.getHighestBlockYAt(x, z);
        }
        Block air = world.getBlockAt(x, (y + 1), z);
        if (air.getType() == Material.AIR) {
            y = world.getHighestBlockYAt(x, z);
        }
        Block water = world.getBlockAt(x, (y - 1), z);
        if (water.getTypeId() == 9) {
            RandomTp.cooldown.remove(player.getName());
            player.sendMessage(getPmTTC("&cVocê nasceu na água, Se quiser pode repetir o /rtp"));
            y = world.getHighestBlockYAt(x, z) - 1;
            Block glass = world.getBlockAt(x, y, z);
            glass.setType(Material.GLASS);
        }
        return new Location(world, x + 0.5, y + 1, z + 0.5);
    }
    public static Location getRandomTpDims(World world) {
        int x, y, z;
        x = getRandomCoordinate();
        z = getRandomCoordinate();
        y = random.nextInt(200);
        Block block = world.getBlockAt(x, y, z);
        if (block.getType() != Material.AIR) {
            y = world.getHighestBlockYAt(x, z);
        }
        Block air = world.getBlockAt(x, (y + 1), z);
        if (air.getType() == Material.AIR) {
            y = world.getHighestBlockYAt(x, z);
        }
        return new Location(world, x + 0.5, y + 1, z + 0.5);
    }

    public static Location getRandomTpNether(World world, Player player) {
        Location location = null;
        while (location == null) {
            int x, y, z;
            x = getRandomCoordinate();
            z = getRandomCoordinate();
            y = random.nextInt(71) + 10;
            Block block = null;
            double distanciaMaisProxima = Double.MAX_VALUE;
            for (int dx = -1; dx <= 1; dx++) {
                for (int dy = -1; dy <= 1; dy++) {
                    for (int dz = -1; dz <= 1; dz++) {
                        Block blocoAdjacente = world.getBlockAt(x + dx, y + dy, z + dz);
                        if (blocoAdjacente.getType() == Material.AIR) {
                            double distancia = Math.sqrt(dx * dx + dy * dy + dz * dz);
                            if (distancia < distanciaMaisProxima) {
                                distanciaMaisProxima = distancia;
                                block = blocoAdjacente;
                            }
                        }
                    }
                }
            }
            if (block != null) {
                if (block.getType() == Material.AIR) {
                    for (int dy = block.getY(); dy >= 1; dy--) {
                        Block newblock = world.getBlockAt(block.getX(), dy, block.getZ());
                        if (newblock.getType() != Material.AIR) {
                            block = world.getBlockAt(block.getX(), dy, block.getZ());
                            break;
                        }
                    }
                }
                Block lava = world.getBlockAt(block.getX(), (block.getY() - 1), block.getZ());
                if (lava.getTypeId() == 11) {
                    player.sendMessage(getPmTTC("&cCuidado você nasceu na lava, Se quiser pode repetir o comando /rtp"));
                    RandomTp.cooldown.remove(player.getName());
                    for (int dx = -1; dx <= 1; dx++) {
                        for (int dz = -1; dz <= 1; dz++) {
                            Block glass = world.getBlockAt(block.getX() + dx, block.getY(), block.getZ() + dz);
                            glass.setType(Material.GLASS);
                        }
                    }
                }
                location = new Location(world, block.getLocation().getX() + 0.5, block.getLocation().getY() + 1, block.getLocation().getZ() + 0.5);
            }
        }
        return location;
    }

    private static int getRandomCoordinate() {
        return random.nextInt(MAX_RANGE) - MAX_RANGE / 2;
    }

}
