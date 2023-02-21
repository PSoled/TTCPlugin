package com.totalcraft.soled;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import ru.tehkode.permissions.PermissionUser;
import ru.tehkode.permissions.bukkit.PermissionsEx;

import java.util.HashMap;
import java.util.Map;

import static com.totalcraft.soled.Main.permissionAdm;

public class prefixMsgs {

    public static String getTTCMessage(String message) {
        return ChatColor.translateAlternateColorCodes('&', "&7&l[&6&lTTC&7&l]&r ") + ChatColor.translateAlternateColorCodes('&', message);
    }

    public static String getRankupMessage(String playerName, String rankName) {
        return getTTCMessage("&f&lO Player &b&l" + playerName + " &f&lUpou para o Rank " + rankName);
    }

    public static String getNotAdm() {
        return getTTCMessage("&cEste comando só pode ser usado por um ADM!");
    }

    public static String getErroMessage(int msgErro) {
        return getTTCMessage("&cVocê precisa de " + msgErro + " Shop Upgrade para upar seu Rank!&r ");
    }

    public static String getRankMaxMessage() {
        return getTTCMessage("&cVocê está no rank Máximo!&r ");
    }

    public static String getCommandOff() {
        return getTTCMessage("&cEste comando foi desativado pelo administrador.");
    }

    public static String getConsoleMessage() {
        return ChatColor.RED + "Este comando só pode ser executado por um jogador.";
    }

    public static String getCommandAdm(String commandOrEvent, String command, boolean onOff) {
        if (onOff) {
            return getTTCMessage("&fO " + commandOrEvent + " &e" + command + " &ffoi &aAtivado &fcom sucesso.");
        } else {
            return getTTCMessage("&fO " + commandOrEvent + " &e" + command + " &ffoi &cDesativado &fcom sucesso.");
        }
    }

    public static String getRankupMessageRank(String playerName, int Rank) {
        if (Rank == 1) {
            return getRankupMessage(playerName, "&7&lPedra");
        } else if (Rank == 2) {
            return getRankupMessage(playerName, "&8&lCarvão");
        } else if (Rank == 3) {
            return getRankupMessage(playerName, "&f&lFerro");
        } else if (Rank == 4) {
            return getRankupMessage(playerName, "&e&lOuro");
        } else if (Rank == 5) {
            return getRankupMessage(playerName, "&b&lDiamante");
        } else if (Rank == 6) {
            return getRankupMessage(playerName, "&a&lEsmeralda");
        } else if (Rank == 7) {
            return getRankupMessage(playerName, "&d&lNetherStar");
        }
        return null;
    }

    public static String getlistModule() {
        Map<String, String[]> moduleStatus = new HashMap<>();
        moduleStatus.put(ChatColor.BLUE + Modules.rankup, new String[]{"Rankup", Main.rankupModule ? ChatColor.GREEN + " On" : ChatColor.RED + " Off"});
        moduleStatus.put(ChatColor.DARK_AQUA + Modules.groupchange, new String[]{"Groupchange", Main.eventGroupChangeModule ? ChatColor.GREEN + " On" : ChatColor.RED + " Off"});

        StringBuilder sb = new StringBuilder();
        sb.append(getTTCMessage("&bLista dos modules que podem desativar e ativar\n\n"));

        for (String module : moduleStatus.keySet()) {
            String[] moduleData = moduleStatus.get(module);
            sb.append(module).append(ChatColor.YELLOW).append(" ").append(moduleData[0]).append(moduleData[1]).append("\n");
        }

        return sb.toString();
    }

    public static String getCommandsMina(CommandSender sender) {
       String msg = getTTCMessage("&fLista de comandos da Mina\n" +
                "\n&a/mina entrar &eEntrar no Evento Mina" +
                "\n&a/mina sair &eSair do Evento Mina" +
                "\n&c/mina start &b(Opcional): &e<Segundos> <Minutos>" +
                "\n&cObs: &fO start Mina por padrão é 120 Segundos para Iniciar e 5 Minutos de duração" +
                "\n&c/mina stop &eParar todo Evento Mina");

        if (!(sender instanceof Player)) {
            return msg;
        }
        Player player = (Player) sender;
        PermissionUser user = PermissionsEx.getUser(player);
        if (user.has(permissionAdm)) {
            return msg;
        } else {
            msg = getTTCMessage("&fLista de comandos da Mina\n" +
                    "\n&a/mina entrar &eEntrar no Evento Mina" +
                    "\n&a/mina sair &eSair do Evento Mina");
        }
        return msg;
    }
}