package com.totalcraft.soled.Utils;

import com.totalcraft.soled.Commands.Modules;
import com.totalcraft.soled.Configs.MainConfig;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class PrefixMsgs {
    static Utils utils = new Utils();

    public static String getPmTTC(String message) {
        return ChatColor.translateAlternateColorCodes('&', "&7&l[&6&lTTC&7&l]&r ") + ChatColor.translateAlternateColorCodes('&', message);
    }

    public static String getPmRankup(String playerName, String rankName) {
        return getPmTTC("&f&lO Player &b&l" + playerName + " &f&lUpou para o Rank " + rankName);
    }

    public static String getPmNotAdm() {
        return getPmTTC("&cEste comando só pode ser usado por um ADM!");
    }

    public static String getPmErro(int msgErro) {
        return getPmTTC("&cVocê precisa de " + msgErro + " Shop Upgrade para upar seu Rank!&r ");
    }

    public static String getPmRankMax() {
        return getPmTTC("&cVocê está no rank Máximo!&r ");
    }

    public static String getPmCommandOff() {
        return getPmTTC("&cEste comando foi desativado pelo administrador.");
    }

    public static String getPmConsole() {
        return ChatColor.RED + "Este comando só pode ser executado por um jogador.";
    }

    public static String getPmCommandAdm(String commandOrEvent, String command, boolean onOff) {
        if (onOff) {
            return getPmTTC("&fO " + commandOrEvent + " &e" + command + " &ffoi &aAtivado &fcom sucesso.");
        } else {
            return getPmTTC("&fO " + commandOrEvent + " &e" + command + " &ffoi &cDesativado &fcom sucesso.");
        }
    }

    public static String getPmRankupRank(String playerName, int Rank) {
        if (Rank == 1) {
            return getPmRankup(playerName, "&7&lPedra");
        } else if (Rank == 2) {
            return getPmRankup(playerName, "&8&lCarvão");
        } else if (Rank == 3) {
            return getPmRankup(playerName, "&f&lFerro");
        } else if (Rank == 4) {
            return getPmRankup(playerName, "&e&lOuro");
        } else if (Rank == 5) {
            return getPmRankup(playerName, "&b&lDiamante");
        } else if (Rank == 6) {
            return getPmRankup(playerName, "&a&lEsmeralda");
        } else if (Rank == 7) {
            return getPmRankup(playerName, "&d&lNetherStar");
        }
        return null;
    }

    public static String getListModule() {
        Map<String, String[]> moduleStatus = new HashMap<>();
        moduleStatus.put(ChatColor.BLUE + Modules.rankup, new String[]{"Rankup", MainConfig.rankupModule ? ChatColor.GREEN + " On" : ChatColor.RED + " Off"});
        moduleStatus.put(ChatColor.DARK_AQUA + Modules.groupchange, new String[]{"Groupchange", MainConfig.eventGroupChangeModule ? ChatColor.GREEN + " On" : ChatColor.RED + " Off"});

        StringBuilder sb = new StringBuilder();
        sb.append(getPmTTC("&bLista dos modules que podem desativar e ativar\n\n"));

        for (String module : moduleStatus.keySet()) {
            String[] moduleData = moduleStatus.get(module);
            sb.append(module).append(ChatColor.YELLOW).append(" ").append(moduleData[0]).append(moduleData[1]).append("\n");
        }

        return sb.toString();
    }

    public static String getCommandsMina(CommandSender sender) {
        String msg = getPmTTC("&fLista de comandos da Mina\n" +
                "\n&a/mina entrar &eEntrar no Evento Mina" +
                "\n&a/mina sair &eSair do Evento Mina" +
                "\n&c/mina start &b(Opcional): &e<Segundos> <Minutos>" +
                "\n&cObs: &fO start Mina por padrão é 120 Segundos para Iniciar e 5 Minutos de duração" +
                "\n&c/mina stop &eParar todo Evento Mina" +
                "\n&c/mina reset &eResetar blocos da Mina" +
                "\n&c/mina stats &eVer Status da Mina");

        if (!(sender instanceof Player)) {
            return msg;
        }
        if (utils.getAdm(sender)) {
            msg = getPmTTC("&fLista de comandos da Mina\n" +
                    "\n&a/mina entrar &eEntrar no Evento Mina" +
                    "\n&a/mina sair &eSair do Evento Mina");
        }
        return msg;
    }

    public static String getCommandsPlugin() {
        String msg = getPmTTC("&fLista de comandos do Plugin\n" +
                "\n&c/ttcsoled reload &eRenicia o Plugin TTCSoled" +
                "\n&c/ttcsoled restartserver &eRenicia o Server" +
                "\n&c/ttcsoled som &e<Player> <Som>");
        return msg;
    }
}