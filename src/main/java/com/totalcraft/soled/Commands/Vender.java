package com.totalcraft.soled.Commands;

import com.totalcraft.soled.Configs.MainConfig;
import com.totalcraft.soled.Utils.Utils;
import com.totalcraft.soled.Utils.VenderUtils;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import ru.tehkode.permissions.PermissionUser;
import ru.tehkode.permissions.bukkit.PermissionsEx;

import static com.totalcraft.soled.Utils.PrefixMsgs.*;


public class Vender implements CommandExecutor {
    Utils utils = new Utils();
    VenderUtils venderUtils = new VenderUtils();
    Economy economy = Bukkit.getServicesManager().getRegistration(Economy.class).getProvider();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("vender")) {
            switch (args.length > 0 ? args[0].toLowerCase() : "") {
                case "itens":
                case "setitem":
                    break;
                default:
                    sender.sendMessage(getCommandsVender(sender));
                    break;
            }
            if (args.length > 0 && args[0].equalsIgnoreCase("itens")) {
                if (!(sender instanceof Player)) {
                    sender.sendMessage(getPmConsole());
                    return true;
                }
                if (!MainConfig.venderModule) {
                    sender.sendMessage(getPmTTC("&cComando /vender Desativado"));
                    return true;
                }
                Player player = (Player) sender;
                PermissionUser user = PermissionsEx.getUser(player);

                if (!user.has("ttcsoled.vender")) {
                    player.sendMessage(getPmTTC("&cVocê não tem acesso ao /vender"));
                    return true;
                }
                double valor = 0;
                double valornether = 0;

                if (args.length > 1 && args[1].equalsIgnoreCase("netherstar")) {
                    if (user.has("rankNetherstar")) {
                        valornether = venderUtils.getAmount(player, VenderUtils.priceItemsNether());
                    } else {
                        player.sendMessage(getPmTTC("&cVocê não é Rank Nether Star!"));
                        return true;
                    }
                } else {
                    valor = venderUtils.getAmount(player, VenderUtils.priceItems);
                }
                if (valor == 0 && valornether == 0) {
                    player.sendMessage(getPmTTC("&cVocê não tem itens para vender"));
                    return true;
                }

                String playerName = player.getName();
                double profit = venderUtils.getProfit(player);
                double total = (valor * profit / 100) + valor + (valornether * profit / 100) + valornether;
                economy.depositPlayer(playerName, total);
                player.sendMessage(getPmTTC("&aVocê vendeu os itens no seu inventário por &e" + total + " &aReais!"));
            }


            if (args.length > 0 && args[0].equalsIgnoreCase("setitem")) {
                if (sender instanceof Player) {
                    if (utils.getAdm(sender)) {
                        sender.sendMessage(getPmNotAdm());
                        return true;
                    }
                    if (args.length != 4) {
                        sender.sendMessage(getPmTTC("&cUse: /vender setitem <Id> <Meta> <Valor>"));
                        return true;
                    }
                    if (!args[1].matches("\\d+") || !args[2].matches("\\d+") || !args[3].matches("\\d+")) {
                        sender.sendMessage(getPmTTC("&cTodos valores têm que ser um número inteiro!"));
                        return true;
                    }
                    int id = Integer.parseInt(args[1]);
                    int meta = Integer.parseInt(args[2]);
                    double valor = Double.parseDouble(args[3]);
                    venderUtils.addItem(id, meta, valor);
                    sender.sendMessage(getPmTTC("&bValor do item setado no /vender"));
                }
            }
        }
        return true;
    }
}