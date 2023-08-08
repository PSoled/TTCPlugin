package com.totalcraft.soled.Commands;

import com.totalcraft.soled.PlayTIme.PlayerPT;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static com.totalcraft.soled.PlayTIme.PlayerPT.PlayerPTData;
import static com.totalcraft.soled.Utils.PrefixMsgs.*;
import static com.totalcraft.soled.Utils.Utils.getAdm;

public class PTSoled implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length < 1) {
            sender.sendMessage(getPmTTC("&bComandos do PlayTime"));
            sender.sendMessage("§5>> §3/ptsoled ver §fVer o seu Playtime");
            sender.sendMessage("§5>> §3/ptsoled top §fVer o Playtime Top");
            sender.sendMessage("§5>> §3/ptsoled <nick> §fVer o Playtime de outro jogador");
            return true;
        }
        if (args[0].equalsIgnoreCase("top")) {
            List<PlayerPT> players = new ArrayList<>(PlayerPTData.values());
            players.sort(Comparator.comparingLong(PlayerPT::getReset).reversed());
            List<PlayerPT> top10Players = players.subList(0, Math.min(players.size(), 10));
            sender.sendMessage(getPmTTC("&aPlaytime Top"));
            for (int i = 0; i < top10Players.size(); i++) {
                PlayerPT player = top10Players.get(i);
                sender.sendMessage("§bTop §f" + (i + 1) + " §0-- §6" + player.getName()+ " " + getHours(player.getReset()));
            }
            return true;
        }
        if (args[0].equalsIgnoreCase("ver")) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(getPmTTC("&cDesde quando console tem playtime? Bobinho"));
                return true;
            }
            Player player = (Player) sender;
            PlayerPT playerPT = PlayerPT.getPlayerPT(player.getName());
            assert playerPT != null;
            player.sendMessage(getFormatPT(playerPT));
            return true;
        }
        if (args[0].equalsIgnoreCase("reset")) {
            if (sender instanceof Player) {
                if (getAdm(sender)) {
                    sender.sendMessage(getPmNotAdm());
                    return true;
                }
            }
            if (!(sender instanceof Player)) {
                sender.sendMessage(getPmConsole());
                return true;
            }
            if (args.length != 2) {
                sender.sendMessage(getPmTTC("'-'"));
                return true;
            }
            Player player = (Player) sender;
            PlayerPT playerPT = PlayerPT.getPlayerPT(player.getName());
            if (playerPT != null) {
                if (args[1].equalsIgnoreCase("daily")) {
                    playerPT.resetDaily();
                }
                if (args[1].equalsIgnoreCase("weekly")) {
                    playerPT.resetWeekly();
                }
                if (args[1].equalsIgnoreCase("month")) {
                    playerPT.resetMonth();
                }
                if (args[1].equalsIgnoreCase("reset")) {
                    playerPT.resetReset();
                }
                return true;
            }
        }

        OfflinePlayer player = Bukkit.getOfflinePlayer(args[0]);
        if (!player.hasPlayedBefore()) {
            sender.sendMessage(getPmTTC("&cEste player nunca entrou no Server"));
            return true;
        }
        PlayerPT playerPT = PlayerPT.getPlayerPT(player.getName());
        assert playerPT != null;
        sender.sendMessage(getFormatPT(playerPT));
        return true;
    }


    private String getFormatPT(PlayerPT playerPT) {
        StringBuilder sb = new StringBuilder();
        sb.append(getPmTTC("&aHoras Jogadas"));
        sb.append("\n\n§0>> §6Dia: ").append(getHours(playerPT.getDaily()));
        sb.append("\n§0>> §6Semana: ").append(getHours(playerPT.getWeekly()));
        sb.append("\n§0>> §6Mês: ").append(getHours(playerPT.getMonth()));
        sb.append("\n§0>> §6Reset: ").append(getHours(playerPT.getReset()));
        sb.append("\n§0>> §6Dia Anterior: ").append(getHours(playerPT.getPreviousDaily()));
        sb.append("\n§0>> §6Semana Anterior: ").append(getHours(playerPT.getPreviousWeekly()));
        sb.append("\n§0>> §6Mês Anterior: ").append(getHours(playerPT.getPreviousMonth()));
        int num = 1;
        long resets = playerPT.getReset();
        for (long time : playerPT.getResetHistoric()) {
            sb.append("\n§0>> §6Reset Anterior ").append(num).append(": ").append(getHours(time));
            num++;
            resets += time;
        }
        sb.append("\n\n§0>> §6Total jogado: ").append(getHours(resets));
        return sb.toString();
    }

    private String getHours(long time) {
        long hours = time / 3600;
        long minutes = (time % 3600) / 60;
        long seconds = time % 60;
        String hour, minute, second;
        if (hours <= 1) hour = "Hora";
        else hour = "Horas";
        if (minutes <= 1) minute = "Minuto";
        else minute = "Minutos";
        if (seconds <= 1) second = "Segundo";
        else second = "Segundos";
        if (hours > 0) {
            return "§b" + hours + " §f" + hour + " §b" + minutes + " §f" + minute + " §b" + seconds + " §f" + second;
        }
        if (minutes > 0) {
            return "§b" + minutes + " §f" + minute + " §b" + seconds + " §f" + second;
        }
        if (seconds == 0) {
            return "§bSem dados";
        }
        return "§b" + seconds + " §f" + second;
    }
}
