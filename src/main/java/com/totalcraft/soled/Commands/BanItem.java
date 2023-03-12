package com.totalcraft.soled.Commands;

import com.totalcraft.soled.Configs.MainConfig;
import com.totalcraft.soled.Utils.Utils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static com.totalcraft.soled.Configs.MainConfig.banItemList;
import static com.totalcraft.soled.Utils.PrefixMsgs.*;

public class BanItem implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            if (Utils.getAdm(sender)) {
                sender.sendMessage(getPmNotAdm());
                return true;
            }
        }
        if (cmd.getName().equalsIgnoreCase("banitem")) {
            switch (args.length > 0 ? args[0].toLowerCase() : "") {
                case "add":
                case "remove":
                    break;
                default:
                    sender.sendMessage(getCommandBanItem());
                    break;
            }

            if (args.length > 0 && args[0].equalsIgnoreCase("add")) {
                if (args.length != 3) {
                    sender.sendMessage(getPmTTC("&cUse /banitem add <Id> <Meta>"));
                    return true;
                }
                int id, meta;
                try {
                    id = Integer.parseInt(args[1]);
                    meta = Integer.parseInt(args[2]);
                } catch (NumberFormatException a) {
                    sender.sendMessage(getPmTTC("&cTem alguma coisa errado ai meu filho") + a.getMessage());
                    a.printStackTrace();
                    return true;
                }

                banItemList.add(id + ":" + meta);
                MainConfig.saveBanItem();
                sender.sendMessage(getPmTTC("&bItem Banido com Sucesso"));
            }
            if (args.length > 0 && args[0].equalsIgnoreCase("remove")) {
                if (args.length != 3) {
                    sender.sendMessage(getPmTTC("&cUse /banitem remove <Id> <Meta>"));
                    return true;
                }
                int id, meta;
                try {
                    id = Integer.parseInt(args[1]);
                    meta = Integer.parseInt(args[2]);
                } catch (NumberFormatException a) {
                    sender.sendMessage(getPmTTC("&cTem alguma coisa errado ai meu filho"));
                    return true;
                }
                banItemList.remove(id + ":" + meta);
                MainConfig.saveBanItem();
                sender.sendMessage(getPmTTC("&bItem Desbanido com Sucesso"));
            }
        }
        return true;
    }
}
