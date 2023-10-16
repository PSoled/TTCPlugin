package com.totalcraft.soled.Commands;

import com.totalcraft.soled.PlayTIme.PlayerPT;
import com.totalcraft.soled.Utils.RestartServerUtils;
import com.totalcraft.soled.Utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import ru.tehkode.permissions.PermissionUser;
import ru.tehkode.permissions.bukkit.PermissionsEx;

import static com.totalcraft.soled.Tasks.MemoryRam.*;
import static com.totalcraft.soled.Utils.PrefixMsgs.*;
import static org.bukkit.Sound.ORB_PICKUP;

public class CommandsPlugin implements CommandExecutor {
    private final RestartServerUtils restartServerUtils;

    public CommandsPlugin(Plugin plugin) {
        restartServerUtils = new RestartServerUtils(plugin);
    }
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("ttcsoled")) {
            switch (args.length > 0 ? args[0].toLowerCase() : "") {
                case "restartserver":
                case "som":
                case "teste":
                case "anuncio":
                case "ram":
                case "reloadbukkit":
                    break;
                default:
                    sender.sendMessage(getCommandsPlugin());
                    break;
            }
            if (args.length > 0 && args[0].equalsIgnoreCase("anuncio")) {
                if (sender instanceof Player) {
                    Player player = (Player) sender;
                    PermissionUser user = PermissionsEx.getUser(player);
                    if (!user.has("ttcsoled.anuncio")) {
                        player.sendMessage(getPmNotAdm());
                        return true;
                    }
                }
                if (args.length < 2) {
                    sender.sendMessage(getPmTTC("&cUse /ttcsoled anuncio <Msg>"));
                    return true;
                }
                StringBuilder sb = new StringBuilder("§b-=§f§l[§d§lEventoTTC§f§l]§b=- §b");
                for (int i = 1; i < args.length; i++) {
                    sb.append(args[i]).append(" ");
                }
                for (Player player : Bukkit.getOnlinePlayers()) {
                    player.playSound(player.getLocation(), ORB_PICKUP, 1, 1);
                }
                Bukkit.broadcastMessage("");
                Bukkit.broadcastMessage(sb.toString());
                Bukkit.broadcastMessage("");
                return true;
            }
            if (sender instanceof Player) {
                if (Utils.getAdm(sender)) {
                    sender.sendMessage(getPmNotAdm());
                    return true;
                }
            }
            if (args.length > 0 && args[0].equalsIgnoreCase("restartserver")) {
                restartServerUtils.restartServer();
            }
            if (args.length > 0 && args[0].equalsIgnoreCase("som")) {
                if (args.length != 3) {
                    sender.sendMessage(getPmTTC("&cUse: /ttcsoled som <Player> <Som>"));
                    return true;
                }
                Player player = Bukkit.getPlayer(args[1]);
                if (player == null) {
                    sender.sendMessage(getPmTTC("&cEste Player não está online!"));
                    return true;
                }
                try {
                    Sound som = Sound.valueOf(args[2]);
                    player.playSound(player.getLocation(), som, 1, 1);
                } catch (IllegalArgumentException e) {
                    player.sendMessage(getPmTTC("&cO som &b" + args[2] + " &cnão existe no Minecraft."));
                }
            }
            if (args.length > 0 && args[0].equalsIgnoreCase("reloadbukkit")) {
                Bukkit.reload();
            }

            if (args.length > 0 && args[0].equalsIgnoreCase("ram")) {
                long totalMemory = Runtime.getRuntime().totalMemory();
                long freeMemory = Runtime.getRuntime().freeMemory();
                long maxMemory = Runtime.getRuntime().maxMemory();
                long usedMemory = totalMemory - freeMemory;
                double usedMemoryMB = (double) usedMemory / (1024 * 1024);
                double maxMemoryMB = (double) maxMemory / (1024 * 1024);
                String useMem = String.valueOf(usedMemoryMB);
                sender.sendMessage("Memória máxima: " + maxMemoryMB + " MB");
                sender.sendMessage("Memória usada: " + useMem.split("\\.")[0] + " MB");
                sender.sendMessage("Ram 5 Segs: " + RAM_5SEG);
                sender.sendMessage("Ram 1 Min: " + RAM_1MIN);
                sender.sendMessage("Ram 5 Mins: " + RAM_5MIN);
                sender.sendMessage("Ram 15 Mins: " + RAM_15MIN);
                sender.sendMessage("Ram 30 Mins: " + RAM_30MIN);
            }
            if (args.length > 0 && args[0].equalsIgnoreCase("teste")) {
                PlayerPT playerPT = PlayerPT.getPlayerPT(((Player) sender).getName());
                assert playerPT != null;
                playerPT.resetReset();
            }
        }
        return true;
    }
}

