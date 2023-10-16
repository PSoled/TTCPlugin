package com.totalcraft.soled.Commands;

import com.totalcraft.soled.PlayerManager.PlayerBase;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.totalcraft.soled.PlayerManager.PlayerBase.getPlayerBase;
import static com.totalcraft.soled.Utils.PrefixMsgs.*;
import static com.totalcraft.soled.Utils.Utils.getAdm;

public class Recompensa implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        switch (args.length > 0 ? args[0].toLowerCase() : "") {
            case "coletar":
            case "ver":
            case "give":
                break;
            default:
                sender.sendMessage(getInfoRecompensa(sender));
                break;
        }
        if (args.length > 0) {
            if (args[0].equalsIgnoreCase("coletar")) {
                if (!(sender instanceof Player)) {
                    sender.sendMessage(getPmConsole());
                    return true;
                }
//                if (getAdm(sender)) {
//                    sender.sendMessage(getPmTTC("&cComando desativado temporariamente."));
//                    return true;
//                }
                Player player = (Player) sender;
                PlayerBase playerBase = PlayerBase.getPlayerBase(player.getName());
                if (playerBase != null) {
                    if (player.getInventory().firstEmpty() == -1) {
                        player.sendMessage(getPmTTC("&cVocê precisa de ao menos 1 slot para receber os itens"));
                        return true;
                    }
                    if (playerBase.getItemsGive().size() == 0) {
                        player.sendMessage(getPmTTC("&cVocê não tem itens para coletar na recompensa"));
                        return true;
                    }
                    List<ItemStack> copy = new ArrayList<>(playerBase.getItemsGive());
                    Inventory inv = player.getInventory();
                    for (ItemStack item : copy) {
                        for (int i = 0; i < inv.getSize(); i++) {
                            if (inv.getItem(i) == null) {
                                inv.setItem(i, item);
                                break;
                            }
                        }
                        if (player.getInventory().firstEmpty() == -1) break;
                        playerBase.getItemsGive().remove(item);
                        System.out.println(item);
                    }
                    playerBase.saveData();
                    player.sendMessage(getPmTTC("&aSuas recompensas foram coletadas"));
                    if (playerBase.getItemsGive().size() > 0) {
                        player.sendMessage(getPmTTC("&cPorém ainda existem itens que podem ser coletados"));
                    }
                }
            }
            if (args[0].equalsIgnoreCase("give")) {
                if (sender instanceof Player) {
                    if (getAdm(sender)) {
                        sender.sendMessage(getPmNotAdm());
                        return true;
                    }
                }
                if (args.length != 5) {
                    sender.sendMessage(getPmTTC("&cUse: /recompensa give <nick> <id> <meta> <quantidade>"));
                    return true;
                }
                PlayerBase playerBase = getPlayerBase(args[1]);
                if (playerBase == null) {
                    sender.sendMessage(getPmTTC("&cEste player não contém no banco de dados. Provavelmente nunca entrou no servidor"));
                    return true;
                }
                int id = Integer.parseInt(args[2]);
                short meta = Short.parseShort(args[3]);
                int amount = Integer.parseInt(args[4]);
                playerBase.addItemsGive(id, meta, amount);
                playerBase.saveData();
                Player target = Bukkit.getPlayer(playerBase.getName());
                ItemStack item = new ItemStack(id, amount, meta);
                if (target != null) {
                    target.sendMessage(getPmTTC("&aAdicionado item &f" + item.getType().name() + ":" + item.getDurability() + " &aNa sua Recompensa, Utilize &b/recompensa coletar &apara pegar"));
                }
                sender.sendMessage(getPmTTC("&aAdicionado item &f" + item.getType().name() + ":" + item.getDurability() + " &ade Recompensa para &f" + args[1] + " &aCom Sucesso"));
            }

            if (args[0].equalsIgnoreCase("ver")) {
                if (!(sender instanceof Player)) {
                    sender.sendMessage(getPmConsole());
                    return true;
                }
                Player player = (Player) sender;
                PlayerBase playerBase = PlayerBase.getPlayerBase(player.getName());
                if (playerBase != null) {
                    if (playerBase.getItemsGive().size() == 0) {
                        player.sendMessage(getPmTTC("&cVocê não tem nenhuma recompensa"));
                        return true;
                    }
                    Map<String, Integer> items = new HashMap<>();
                    for (ItemStack item : playerBase.getItemsGive()) {
                        String s = item.getTypeId() + " " + item.getDurability();
                        if (items.containsKey(s)) {
                            items.put(s, items.get(s) + item.getAmount());
                        } else {
                            items.put(s, item.getAmount());
                        }
                    }
                    player.sendMessage(getPmTTC("&aRecompensas para receber"));
                    int num = 1;
                    for (Map.Entry<String, Integer> entry : items.entrySet()) {
                        String[] s = entry.getKey().split(" ");
                        player.sendMessage("§5>> §eItem §f" + num + "§e: §6ID §f" + s[0] + " §6Meta §f" + s[1] + " §6Quantidade §f" + entry.getValue());
                        num++;
                    }
                }
            }
        }
        return true;
    }
}

