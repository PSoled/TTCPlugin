package com.totalcraft.soled.Commands;

import com.totalcraft.soled.PlayerManager.PlayerBase;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

import static com.totalcraft.soled.Utils.PrefixMsgs.getPmConsole;
import static com.totalcraft.soled.Utils.PrefixMsgs.getPmTTC;

public class SaveGod implements CommandExecutor {
    List<Integer> Items = Arrays.asList(4386, 4387, 4388, 4389, 4391, 4270, 4271, 4272, 4273);
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(getPmConsole());
            return true;
        }
        Player player = (Player) sender;
        if (player.getItemInHand() == null || player.getItemInHand().getType() == Material.AIR) {
            player.sendMessage(getPmTTC("&cVocê deve segurar o Item God em sua mão"));
            return true;
        }
        if (!Items.contains(player.getItemInHand().getTypeId())) {
            player.sendMessage(getPmTTC("&cEste não é um item God que pode ser salvo"));
            return true;
        }
        if (player.getItemInHand().getItemMeta() == null || player.getItemInHand().getItemMeta().getLore() == null) {
            player.sendMessage(getPmTTC("&CVocê deve privar este Item God no seu nome para pode salvar ele"));
            return true;
        }
        boolean nick = false;
        for (String s : player.getItemInHand().getItemMeta().getLore()) {
            if (s.contains(player.getName())) nick = true;
         }
        if (!nick) {
            player.sendMessage(getPmTTC("&cEste Item God não está no seu privado para que você possa salvar ele"));
            return true;
        }
        PlayerBase playerBase = PlayerBase.getPlayerBase(player.getName());
        String s = player.getItemInHand().getTypeId() + ":" + player.getItemInHand().getDurability();
        assert playerBase != null;
        playerBase.setSaveGod(s);
        player.sendMessage(getPmTTC("&aItem God salvo com Sucesso para receber no próximo reset"));
        return true;
    }
}
