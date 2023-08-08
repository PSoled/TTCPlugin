package com.totalcraft.soled.Commands;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import ru.tehkode.permissions.PermissionUser;
import ru.tehkode.permissions.bukkit.PermissionsEx;

import static com.totalcraft.soled.Utils.PrefixMsgs.getPmConsole;
import static com.totalcraft.soled.Utils.PrefixMsgs.getPmTTC;

public class Repair implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(getPmConsole());
            return true;
        }
        Player player = (Player) sender;
        PermissionUser user = PermissionsEx.getUser(player);
        if (!user.has("ttcsoled.repair") && !player.isOp()) {
            player.sendMessage(getPmTTC("&cVocê não tem permissão para executar este comando."));
            return true;
        }
        ItemStack hand = player.getItemInHand();
        if (hand == null || hand.getType() == Material.AIR) {
            player.sendMessage(getPmTTC("&cReparar o vento é algo de muita complexidade meu amigo..."));
            return true;
        }
        Material material = hand.getType();
        if (material.name().startsWith("WOOD_") || material.name().startsWith("STONE_") ||
                material.name().startsWith("IRON_") || material.name().startsWith("GOLD_") ||
                material.name().startsWith("DIAMOND_") || material.name().startsWith("LEATHER_")) {
            hand.setDurability((short) 0);
            player.setItemInHand(hand);
            player.sendMessage(getPmTTC("&aitem reparado com sucesso!"));
            return true;
        }
        player.sendMessage(getPmTTC("&cO item na sua mão não pode ser reparado"));
        return true;
    }
}
