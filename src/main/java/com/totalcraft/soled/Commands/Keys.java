package com.totalcraft.soled.Commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

import static com.totalcraft.soled.Crates.CrateBase.configKey;
import static com.totalcraft.soled.Crates.KeysShop.invKeysShop;
import static com.totalcraft.soled.Utils.PrefixMsgs.*;
import static com.totalcraft.soled.Utils.Utils.getAdm;

public class Keys implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length == 0) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(getPmConsole());
                return true;
            }
            Player player = (Player) sender;
            player.openInventory(invKeysShop);
            return true;
        }
        if (args[0].equalsIgnoreCase("give")) {
            if (sender instanceof Player) {
                if (getAdm(sender)) {
                    sender.sendMessage(getPmNotAdm());
                    return true;
                }
            }
            if (args.length != 4) {
                sender.sendMessage(getPmTTC("&cUse: /key give <Nick> <Keyname> <Quantidade>"));
                return true;
            }

            Player player = Bukkit.getPlayer(args[1]);
            if (player == null) {
                sender.sendMessage(getPmTTC("Player offline"));
                return true;
            }
            int amount = Integer.parseInt(args[3]);
            giveKeyCrate(sender, player, amount, args[2]);
        }
        return true;
    }
    public static void giveKeyCrate(CommandSender sender, Player player, int amount, String keyName) {
        ItemStack key = new ItemStack(configKey.getType(), amount, configKey.getDurability());
        ItemMeta meta = key.getItemMeta();
        List<String> lore = new ArrayList<>();
        lore.add("");
        lore.add(getFormatColor("&7Key:&c " + keyName));
        meta.setLore(lore);
        meta.setDisplayName(getPmTTC("&bKey Crate"));
        key.setItemMeta(meta);
        player.getInventory().addItem(key);
        sender.sendMessage(getPmTTC(getFormatColor("&eKey:&c " + keyName) + " &eGivado para &b" + player.getName() + " &ecom Sucesso"));
    }
}
