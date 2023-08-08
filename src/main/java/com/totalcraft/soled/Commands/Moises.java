package com.totalcraft.soled.Commands;

import me.ryanhamshire.GriefPrevention.Claim;
import me.ryanhamshire.GriefPrevention.GriefPrevention;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

import static com.totalcraft.soled.Utils.PrefixMsgs.getPmConsole;
import static com.totalcraft.soled.Utils.PrefixMsgs.getPmTTC;

public class Moises implements CommandExecutor {
    List<Integer> blocksMoises = Arrays.asList(8, 9, 10, 11);
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(getPmConsole());
            return true;
        }
        Player player = (Player) sender;
        boolean rs = false;
        Claim claim = GriefPrevention.instance.dataStore.getClaimAt(player.getLocation(), false, null);
        if (claim != null) {
            if (claim.getOwnerName().equalsIgnoreCase(player.getName())) {
                Location lesserBoundaryCorner = claim.getLesserBoundaryCorner();
                Location greaterBoundaryCorner = claim.getGreaterBoundaryCorner();
                int x1 = lesserBoundaryCorner.getBlockX();
                int z1 = lesserBoundaryCorner.getBlockZ();
                int x2 = greaterBoundaryCorner.getBlockX();
                int z2 = greaterBoundaryCorner.getBlockZ();
                int minX = Math.min(x1, x2);
                int minY = 0;
                int minZ = Math.min(z1, z2);
                int maxX = Math.max(x1, x2);
                int maxY = 255;
                int maxZ = Math.max(z1, z2);
                World world = player.getWorld();
                for (int x = minX; x <= maxX; x++) {
                    for (int y = minY; y <= maxY; y++) {
                        for (int z = minZ; z <= maxZ; z++) {
                            Block block = world.getBlockAt(x, y, z);
                            if (blocksMoises.contains(block.getTypeId())) {
                                block.setType(Material.AIR);
                                rs = true;
                            }
                        }
                    }
                }
                if (rs) {
                    player.sendMessage(getPmTTC("&aMoisés passou por aqui e rapou até a lava do terreno"));
                } else {
                    player.sendMessage(getPmTTC("&cMOISÉS FICA TRISTE POIS NÃO ENCONTROU AGUA PARA PASSAR"));
                }
            } else {
                player.sendMessage(getPmTTC("&cVocê não é dono deste terreno"));
            }
        }
        return true;
    }
}
