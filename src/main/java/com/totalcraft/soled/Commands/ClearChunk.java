package com.totalcraft.soled.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import static com.totalcraft.soled.Listeners.PlayerInteract.listClearChunk;
import static com.totalcraft.soled.Utils.ClearChunkUtils.blocksChunk;
import static com.totalcraft.soled.Utils.ClearChunkUtils.clearChunk;
import static com.totalcraft.soled.Utils.PrefixMsgs.*;

public class ClearChunk implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(getPmConsole());
            return true;
        }
        Player player = (Player) sender;
        if (cmd.getName().equalsIgnoreCase("clearchunk")) {
            switch (args.length > 0 ? args[0].toLowerCase() : "") {
                case "cancel":
                case "confirm":
                    break;
                default:
                    sender.sendMessage(getInfoClearChunk());
                    break;
            }
            if (args.length > 0 && args[0].equalsIgnoreCase("confirm")) {
                if (!listClearChunk.containsKey(player.getName())) {
                    player.sendMessage(getPmTTC("&cVocê não selecionou a Chunk com o item de limpeza."));
                    return true;
                }
                if (blocksChunk.get(player.getName()) < 50) {
                    player.sendMessage(getPmTTC("&cNão da pra jogar dinheiro fora meu amigo, isto aqui não tem 50 blocos para ser limpo"));
                    player.performCommand("clearchunk cancel");
                    return true;
                }
                boolean question = true;
                for (ItemStack inv : player.getInventory().getContents()) {
                    if (inv != null) {
                        if (inv.getTypeId() == 27192) {
                            question = false;
                            if (inv.getAmount() == 1) {
                                player.getInventory().remove(inv);
                            } else {
                                inv.setAmount(inv.getAmount() - 1);
                            }
                        }
                    }
                }
                if (question) {
                    player.sendMessage(getPmTTC("&cVocê selecionou a chunk, mas não está com o item se limpeza no seu inventário."));
                    return true;
                }
                clearChunk(player, listClearChunk.get(player.getName()));
                listClearChunk.remove(player.getName());
                player.sendMessage(getPmTTC("&aVocê limpou a chunk"));
            }
            if (args.length > 0 && args[0].equalsIgnoreCase("cancel")) {
                if (!listClearChunk.containsKey(player.getName())) {
                    player.sendMessage(getPmTTC("&cVocê nem tem uma chunk selecionada meu chefe"));
                    return true;
                }
                listClearChunk.remove(player.getName());
                player.sendMessage(getPmTTC("&aFoi cancelado a seleção da limpeza de chunk"));
            }
        }
        return true;
    }
}
