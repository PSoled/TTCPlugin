package com.totalcraft.soled.Commands;

import com.totalcraft.soled.Configs.MainConfig;
import com.totalcraft.soled.Utils.RandomTpUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import ru.tehkode.permissions.PermissionUser;
import ru.tehkode.permissions.bukkit.PermissionsEx;

import java.util.HashMap;

import static com.totalcraft.soled.Utils.PrefixMsgs.*;

public class RandomTp implements CommandExecutor {
    public static HashMap<String, Integer> cooldown = new HashMap<>();
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(getPmConsole());
            return true;
        }
        if (!MainConfig.rtpModule) {
            sender.sendMessage(getPmTTC("&cComando Rtp Desativado."));
            return true;
        }
        if (cmd.getName().equalsIgnoreCase("rtp")) {
            switch (args.length > 0 ? args[0].toLowerCase() : "") {
                case "nether":
                case "world":
                case "atum":
                case "twilight":
                case "minerar":
                case "end":
                case "spawn":
                    break;
                default:
                    if (args.length > 0) {
                        sender.sendMessage(getListRtp());
                        return true;
                    }
            }
            Player player = (Player) sender;
            PermissionUser user = PermissionsEx.getUser(player);
            if (!user.has("ttcsoled.bypass.rtp") && !player.isOp()) {
                if (cooldown.containsKey(player.getName())) {
                    player.sendMessage(getPmTTC("&cVocê deve esperar " + cooldown.get(player.getName()) + " Segundos para dar Rtp novamente"));
                    return true;
                }
            }
            World world = player.getWorld();
            RandomTpUtils.randomTpTime();
            if (args.length > 0 && args[0].equalsIgnoreCase("nether")) {
                world = Bukkit.getWorld("DIM-1");
            }
            if (args.length > 0 && args[0].equalsIgnoreCase("world")) {
                world = Bukkit.getWorld("world");
            }
            if (args.length > 0 && args[0].equalsIgnoreCase("atum")) {
                world = Bukkit.getWorld("DIM17");
            }
            if (args.length > 0 && args[0].equalsIgnoreCase("twilight")) {
                world = Bukkit.getWorld("DIM7");
            }
            if (args.length > 0 && args[0].equalsIgnoreCase("minerar")) {
                world = Bukkit.getWorld("minerar");
            }
            if (args.length > 0 && args[0].equalsIgnoreCase("end")) {
                world = Bukkit.getWorld("DIM1");
            }
            if (args.length > 0 && args[0].equalsIgnoreCase("spawn")) {
                world = Bukkit.getWorld("spawn");
            }
            if (world.getName().equals("DIM1") || world.getName().equals("spawn")) {
                cooldown.put(player.getName(), 5);
                player.sendMessage(getPmTTC("&cMeio complicado dar Rtp nesse lugar né meu chefe"));
                return true;
            }
            if (world.getName().equals("DIM-1")) {
                cooldown.put(player.getName(), 30);
                player.teleport(RandomTpUtils.getRandomTpNether(world, player));
            }
            if (world.getName().equals("world") || world.getName().equals("minerar")) {
                cooldown.put(player.getName(), 15);
                player.teleport(RandomTpUtils.getRandomTp(world, player));
            }
            if (world.getName().equals("DIM17") || world.getName().equals("DIM7")) {
                cooldown.put(player.getName(), 30);
                player.teleport(RandomTpUtils.getRandomTpDims(world));
            }
            Location loc = player.getLocation();
            int x = Math.round(loc.getBlockX());
            int y = Math.round(loc.getBlockY());
            int z = Math.round(loc.getBlockZ());
            player.sendMessage(getPmTTC("&bRandomTp Com Sucesso para &6X: &f" + x + " &6Y: &f" + y + " &6Z: &f" + z));
        }
        return true;
    }
}
