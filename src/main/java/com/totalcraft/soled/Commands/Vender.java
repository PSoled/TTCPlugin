package com.totalcraft.soled.Commands;

import com.totalcraft.soled.Configs.MainConfig;
import com.totalcraft.soled.Utils.Utils;
import com.totalcraft.soled.Utils.VenderUtils;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import ru.tehkode.permissions.PermissionUser;
import ru.tehkode.permissions.bukkit.PermissionsEx;

import java.text.DecimalFormat;
import java.util.HashMap;

import static com.totalcraft.soled.Utils.PrefixMsgs.*;


public class Vender implements CommandExecutor {

    Utils utils = new Utils();
    VenderUtils venderUtils = new VenderUtils();
    Economy economy = Bukkit.getServicesManager().getRegistration(Economy.class).getProvider();
    public static DecimalFormat formatter = new DecimalFormat("#,##0.###");
    public static HashMap<String, Integer> playerVender = new HashMap<>();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("vender")) {
            switch (args.length > 0 ? args[0].toLowerCase() : "") {
                case "itens":
                case "setitem":
                case "auto":
                case "adquirir":
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
                double valor;

                valor = VenderUtils.getAmount(player, VenderUtils.priceItems);

                if (valor == 0) {
                    player.sendMessage(getPmTTC("&cVocê não tem itens para vender"));
                    return true;
                }

                String playerName = player.getName();
                double profit = VenderUtils.getProfit(player);
                double total = (valor * profit / 100) + valor;
                economy.depositPlayer(playerName, total);
                String valorTotal = String.format("%s", formatter.format(total));
                player.sendMessage(getPmTTC("&aVocê vendeu os itens no seu inventário por &e" + valorTotal + " &aReais!"));
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
            if (args.length > 0 && args[0].equalsIgnoreCase("auto")) {
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
                if (playerVender.containsKey(player.getName())) {
                    player.sendMessage(getPmTTC("&cVocê já está com o vender auto Ativado"));
                    return true;
                }

                playerVender.put(player.getName(), 0);
                VenderUtils.venderAuto(player, VenderUtils.priceItems);
            }
            if (args.length > 0 && args[0].equalsIgnoreCase("adquirir")) {
                if (!(sender instanceof Player)) {
                    sender.sendMessage(getPmConsole());
                    return true;
                }
                Player player = (Player) sender;
                PermissionUser user = PermissionsEx.getUser(player);

                if (user.has("ttcsoled.vender")) {
                    player.sendMessage(getPmTTC("&cVocê já tem acesso ao /vender"));
                    return true;
                }

                ItemStack fragment = new ItemStack(Material.getMaterial(4374), 1, (short) 0);
                if (!player.getInventory().containsAtLeast(fragment, 1)) {
                    player.sendMessage(getPmTTC("&cVocê precisa de 1 IC2 Fragment para adquirir o /vender"));
                    return true;
                }
                player.getInventory().removeItem(fragment);
                user.addPermission("ttcsoled.vender");
                player.sendMessage(getPmTTC("&aVocê adquiriu acesso ao /vender !!"));
            }
        }
        return true;
    }
}
