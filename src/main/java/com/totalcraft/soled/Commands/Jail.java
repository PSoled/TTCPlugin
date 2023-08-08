package com.totalcraft.soled.Commands;

import com.totalcraft.soled.Configs.MainConfig;
import com.totalcraft.soled.PlayerManager.PlayerBase;
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

import static com.totalcraft.soled.PlayerManager.PlayerBase.playersBase;
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
            PlayerBase playerBase = PlayerBase.getPlayerBase(player.getName());
            if (playerBase == null) return true;
            if (!playerBase.Jail) {
                sender.sendMessage(getPmTTC("&cVocê não está preso"));
                return true;
            }
            int time = playerBase.getJailTime();
            sender.sendMessage(getPmTTC("&cVocê deve ainda " + time + " Minutos de pena"));
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
            PlayerBase playerBase = PlayerBase.getPlayerBase(args[0]);
            if (playerBase == null) {
                sender.sendMessage(getPmTTC("&cEste player nunca entrou no servidor."));
                return true;
            }
            int time;
            try {
                time = Integer.parseInt(args[1]);
            } catch (NumberFormatException a) {
                sender.sendMessage(getPmTTC("&cTem alguma coisa errado ai meu filho"));
                return true;
            }
            Player playerJail = Bukkit.getPlayer(args[0]);
            if (playerBase.Jail) {
                sender.sendMessage(getPmTTC("&cEste Player já está preso!"));
                return true;
            }
            playerBase.Jail = true;
            playerBase.setJailTime(time * 60);
            playerBase.saveData();
            if (playerJail != null) {
                PermissionUser userJail = PermissionsEx.getPermissionManager().getUser(playerJail);
                userJail.setGroups(new String[]{"Prisoners"});
                playerJail.teleport(locationJail);
            }
            Bukkit.broadcastMessage(getPmTTC(playerBase.getName() + " &CFoi preso por " + time + (time == 1 ? " Hora" : " Horas")));
            return true;
        }
        return true;
    }

    public static ScheduledExecutorService schedulerJail = Executors.newScheduledThreadPool(1);
    public static ScheduledFuture<?> scheduledJail;

    public static void jailTime() {
        scheduledJail = schedulerJail.scheduleAtFixedRate(() -> {
            for (String name : playersBase.keySet()) {
                PlayerBase playerBase = playersBase.get(name);
                int time = playerBase.getJailTime();
                if (time == 0 && playerBase.Jail) {
                    Bukkit.broadcastMessage(getPmTTC(playerBase.getName() + " &cCumpriu sua pena da prisão"));
                    Player player = Bukkit.getPlayer(name);
                    if (player != null) {
                        player.teleport(new Location(Bukkit.getWorld("spawn"), 0, 65, 0));
                        playerBase.Jail = false;
                        PermissionUser userJail = PermissionsEx.getPermissionManager().getUser(Bukkit.getPlayer(name));
                        userJail.setGroups(new String[]{"Civil"});
                        playerBase.saveData();
                    }
                }
                if (playerBase.Jail && time > 0) {
                    time--;
                    playerBase.setJailTime(time);
                    playerBase.saveData();
                }
            }
        }, 1, 1, TimeUnit.MINUTES);
    }
}

