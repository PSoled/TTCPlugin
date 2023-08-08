package com.totalcraft.soled.Commands;

import com.Acrobot.Breeze.Utils.MaterialUtil;
import com.Acrobot.Breeze.Utils.StringUtil;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.regex.Matcher;

import static com.totalcraft.soled.Utils.PrefixMsgs.*;
import static com.totalcraft.soled.Utils.Utils.getAdm;

public class SetShop implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(getPmConsole());
            return true;
        }
        if (getAdm(sender)) {
            sender.sendMessage(getPmNotAdm());
            return true;
        }
        Player player = (Player) sender;
        if (args.length != 2) {
            player.sendMessage(getPmTTC("&cUse: /setshop B/S (Valor)"));
            return true;
        }
        Block block = player.getTargetBlock(null, 10);
        if (block == null || block.getType() == Material.AIR || !(block.getState() instanceof Sign)) {
            player.sendMessage(getPmTTC("&cO bloco que você está mirando deve ser uma placa"));
            return true;
        }
        ItemStack item = player.getItemInHand();
        if (item == null || item.getType() == Material.AIR) {
            player.sendMessage(getPmTTC("&cO item na sua mão não pode ser um void né meu parceiro"));
            return true;
        }
        String itemCode = String.valueOf(item.getTypeId());
        if (MaterialUtil.Odd.getFromString(itemCode) == null) {
            String metadata = getMetadata(itemCode);
            String longName = MaterialUtil.getName(item);
            String code;
            if (longName.length() <= 15 - metadata.length() && isSameItem(longName + metadata, item)) {
                code = StringUtil.capitalizeFirstLetter(longName);
                itemCode = code + metadata;
            } else {
                code = MaterialUtil.getName(item, false);
                String[] parts = itemCode.split("(?=:|-|#)", 2);
                String data = parts.length > 1 ? parts[1] : "";
                if (!data.isEmpty() && code.length() > 15 - data.length()) {
                    code = code.substring(0, 15 - data.length());
                }
                if (!isSameItem(code + data, item)) {
                    code = String.valueOf(item.getTypeId());
                }

                code = StringUtil.capitalizeFirstLetter(code);
                itemCode = code + data;
            }
        }
        String argPrice = args[0] + " " + args[1];
        String code = getMetadata(item);
        String meta = item.getDurability() != 0 ? ":" + item.getDurability() : "";
        String IdAndMeta = itemCode + meta + code;
        Sign sign = (Sign) block.getState();
        sign.setLine(0, "TotalShop");
        sign.setLine(1, String.valueOf(item.getAmount()));
        sign.setLine(2, argPrice);
        sign.setLine(3, IdAndMeta);
        sign.update();

        player.sendMessage(getPmTTC("&aSetado shop com Sucesso!"));
        return true;
    }
    private static String getMetadata(ItemStack item) {
        return !item.hasItemMeta() ? "" : "#" + MaterialUtil.Metadata.getItemCode(item);
    }

    private static boolean isSameItem(String newCode, ItemStack item) {
        ItemStack newItem = MaterialUtil.getItem(newCode);
        return newItem != null && MaterialUtil.equals(newItem, item);
    }

    private static String getMetadata(String itemCode) {
        Matcher m = MaterialUtil.METADATA.matcher(itemCode);
        return !m.find() ? "" : m.group();
    }
}
