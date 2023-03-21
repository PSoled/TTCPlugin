package com.totalcraft.soled.Utils;

import com.totalcraft.soled.Configs.MainConfig;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class PrefixMsgs {

    public static String getPmTTC(String message) {
        return ChatColor.translateAlternateColorCodes('&', "&7&l[&6&lTTC&7&l]&r ") + ChatColor.translateAlternateColorCodes('&', message);
    }

    public static String getFormatColor(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
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
        moduleStatus.put("Rankup", new String[]{ChatColor.BLUE + "Comando " + ChatColor.YELLOW + "Rankup", MainConfig.rankupModule ? ChatColor.GREEN + " On" : ChatColor.RED + " Off"});
        moduleStatus.put("Vender", new String[]{ChatColor.BLUE + "Comando " + ChatColor.YELLOW + "Vender", MainConfig.venderModule ? ChatColor.GREEN + " On" : ChatColor.RED + " Off"});
        moduleStatus.put("Rtp", new String[]{ChatColor.BLUE + "Comando " + ChatColor.YELLOW + "Rtp", MainConfig.rtpModule ? ChatColor.GREEN + " On" : ChatColor.RED + " Off"});
        moduleStatus.put("Bcollect", new String[]{ChatColor.BLUE + "Comando " + ChatColor.YELLOW + "Bcollect", MainConfig.bcollectModule ? ChatColor.GREEN + " On" : ChatColor.RED + " Off"});
        moduleStatus.put("EventoGroupChange", new String[]{ChatColor.DARK_AQUA + "Evento " + ChatColor.YELLOW + " Groupchange", MainConfig.eventGroupChangeModule ? ChatColor.GREEN + " On" : ChatColor.RED + " Off"});

        StringBuilder sb = new StringBuilder();
        sb.append(getPmTTC("&bLista dos modules que podem desativar e ativar\n\n"));

        for (String module : moduleStatus.keySet()) {
            String[] moduleData = moduleStatus.get(module);
            sb.append(moduleData[0]).append(moduleData[1]).append("\n");
        }

        return sb.toString();
    }

    public static String getCommandsMina(CommandSender sender) {
        String msg = getPmTTC("&bLista de comandos da Mina\n" +
                "\n&a/mina entrar &eEntrar no Evento Mina" +
                "\n&a/mina sair &eSair do Evento Mina" +
                "\n&c/mina start &b(Opcional): &e<Segundos> <Minutos>" +
                "\n&cObs: &fO start Mina por padrão é 120 Segundos para Iniciar e 5 Minutos de duração" +
                "\n&c/mina stop &eParar todo Evento Mina" +
                "\n&c/mina cancel &eParar o start da Mina" +
                "\n&c/mina reset &eResetar blocos da Mina" +
                "\n&c/mina stats &eVer Status da Mina");

        if (!(sender instanceof Player)) {
            return msg;
        }
        if (Utils.getAdm(sender)) {
            msg = getPmTTC("&bLista de comandos da Mina\n" +
                    "\n&a/mina entrar &eEntrar no Evento Mina" +
                    "\n&a/mina sair &eSair do Evento Mina");
        }
        return msg;
    }

    public static String getCommandsPlugin() {
        return getPmTTC("&bLista de comandos do Plugin\n" +
                "\n&c/ttcsoled reload &eRenicia o Plugin TTCSoled" +
                "\n&c/ttcsoled restartserver &eRenicia o Server" +
                "\n&c/ttcsoled som &e<Player> <Som>");
    }

    public static String getCommandsVender(CommandSender sender) {
        String msg = getPmTTC("&bLista de comandos do Vender\n" +
                "\n&a/vender itens &eVender seus itens" +
                "\n&a/vender auto &eVender Automaticamente seus itens" +
                "\n&a/vender adquirir &eComprar acesso para o /vender" +
                "\n&c/vender setitem &eSeta valor para um item");

        if (!(sender instanceof Player)) {
            return msg;
        }
        if (Utils.getAdm(sender)) {
            msg = getPmTTC("&bLista de comandos do Vender\n" +
                    "\n&a/vender itens &eVender seus itens" +
                    "\n&a/vender auto &eVender Automaticamente seus itens" +
                    "\n&a/vender adquirir &eComprar acesso para o /vender");
        }
        return msg;
    }

    public static String getCommandBP(CommandSender sender) {
        String msg = getPmTTC("&bLista de comandos do BlockProtect\n" +
                "\n&a/blockprotect localizar &eLocalização do seus Blocos Protegidos" +
                "\n&c/blockprotect loc <Player> &eLocalização do Blocos Protegidos do player");

        if (!(sender instanceof Player)) {
            return msg;
        }
        if (Utils.getAdm(sender)) {
            msg = getPmTTC("&bLista de comandos do BlockProtect\n" +
                    "\n&a/blockprotect localizar &eLocalização do seus Blocos Protegidos");
        }
        return msg;
    }

    public static String getCommmandoCB() {
        return getPmTTC("&bLista de comando do CollectBlocks\n" +
                "\n&a/bcollect comprar &eCompra coletor de blocos por 5k por 10Mins" +
//                "\n&a/bcollect filtro <Id> &eFiltra para o ID de bloco seja coletado" +
//                "\n&a/bcollect filtro clear &eLimpar o Filtro" +
                "\n&a/bcollect tempo &eVer tempo restante do coletor de blocos");
    }

    public static String getListRtp() {
        return getPmTTC("&bLista de Comando /rtp\n" +
                "\n&6/rtp &0-- &fRandomTp no mundo que você está." +
                "\n&6/rtp &aworld &0-- &fRandomTp no Mundo Normal." +
                "\n&6/rtp &aminerar &0-- &fRandomTp no Mundo de Minerar." +
                "\n&6/rtp &anether &0-- &fRandomTp no Nether." +
                "\n&6/rtp &aatum &0-- &fRandomTp no Atum." +
                "\n&6/rtp &atwilight &0-- &fRandomTp na Twilight.");
    }

    public static String getCommandBanItem() {
        return getPmTTC("&bLista de Comando do Ban Item\n" +
                "\n&c/banitem add <Id> <Meta> &eEu acho que bane o item :/" +
                "\n&c/banitem remove <Id> <Meta> &eDeve dar unban no item :v");
    }
}