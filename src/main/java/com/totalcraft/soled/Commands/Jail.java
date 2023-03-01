package com.totalcraft.soled.Commands;

import com.totalcraft.soled.Configs.JailData;
import com.totalcraft.soled.Configs.MainConfig;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import ru.tehkode.permissions.PermissionUser;
import ru.tehkode.permissions.bukkit.PermissionsEx;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import static com.totalcraft.soled.Utils.PrefixMsgs.*;

public class Jail implements CommandExecutor {
    public static Location locationJail = new Location(Bukkit.getWorld(MainConfig.worldJail), MainConfig.jailLocationX, MainConfig.jailLocationY, MainConfig.jailLocationZ);

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("jail") && args.length >= 1 && args[0].equalsIgnoreCase("time")) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(getPmConsole());
                return true;
            }
            Player player = (Player) sender;
            String playerName = player.getName();
            if (!JailData.jailListPlayer.containsKey(playerName)) {
                sender.sendMessage(getPmTTC("&cVocê não está preso"));
                return true;
            }
            int time = JailData.jailListPlayer.get(playerName);
            sender.sendMessage(getPmTTC("&cVocê deve ainda " + time + " Horas de pena"));
            return true;
        }

        if (cmd.getName().equalsIgnoreCase("jail")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                PermissionUser user = PermissionsEx.getUser(player);
                if (!user.has("ttcsoled.jail") && !player.isOp()) {
                    sender.sendMessage(getPmNotAdm());
                    return true;
                }
            }
            if (args.length != 2) {
                sender.sendMessage(getPmTTC("&cUse: /jail <Player> <Horas>"));
                return true;
            }
            String playerName = args[0];
            int time = Integer.parseInt(args[1]);
            Player playerJail = Bukkit.getPlayer(playerName);

            if (!args[1].matches("\\d+")) {
                sender.sendMessage(getPmTTC("&cO tempo precisa ser um número inteiro!"));
                return true;
            }

            if (playerJail == null) {
                sender.sendMessage(getPmTTC("&cEste Player não está online!"));
                return true;
            }

            if (JailData.jailListPlayer.containsKey(playerName)) {
                sender.sendMessage(getPmTTC("&cEste Player já está preso!"));
                return true;
            }

            JailData.jailListPlayer.put(playerName, time);
            JailData.saveJailData();

            PermissionUser userJail = PermissionsEx.getPermissionManager().getUser(playerJail);
            userJail.setGroups(new String[]{"Prisoners"});
            playerJail.teleport(locationJail);
            Bukkit.broadcastMessage(getPmTTC(playerName + " &CFoi preso por " + time + (time == 1 ? " Hora" : " Horas")));

            return true;
        }
        return true;
    }

    static ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    static ScheduledFuture<?> scheduledFuture;

    public static void jailTime() {
        scheduledFuture = scheduler.scheduleAtFixedRate(() -> {
            for (String name : JailData.jailListPlayer.keySet()) {
                int valor = JailData.jailListPlayer.get(name);
                if (valor < 1) {
                    Player player = Bukkit.getPlayer(name);
                    JailData.jailListPlayer.remove(name);
                    Bukkit.broadcastMessage(getPmTTC(name + " &cCumpriu sua pena da prisão"));
                    Player playerJail = Bukkit.getPlayer(name);
                    PermissionUser userJail = PermissionsEx.getPermissionManager().getUser(playerJail);
                    userJail.setGroups(new String[]{"Civil"});
                    if (player != null) {
                        Bukkit.dispatchCommand(player, "spawn");
                    }
                    JailData.saveJailData();
                } else {
                    valor--;
                    JailData.jailListPlayer.put(name, valor);
                    JailData.saveJailData();
                }
            }
        }, 1, 1, TimeUnit.HOURS);
    }

    public static void canceljailTime() {
        scheduledFuture.cancel(false);
    }
}
