package com.totalcraft.soled;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.CommandExecutor;
import org.bukkit.entity.Player;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

import static com.totalcraft.soled.prefixMsgs.*;

public class Rankup extends JavaPlugin implements Listener {

    static class RankupCommand implements CommandExecutor {
        RankupUtils rankupUtils = new RankupUtils();
        List<String> ranksList = rankupUtils.ranksList;

        public RankupCommand() {
        }

        public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
            if (sender instanceof Player) {
                Player player = (Player) sender;

                int itemCostRank = rankupUtils.listPriceitem().get(rankupUtils.getNextRank(player, ranksList));
                ItemStack shopUpgrade = new ItemStack(Material.getMaterial(1), itemCostRank, (short) 0);
                String anuncioRank = getRankupMessageRank(player.getName(), rankupUtils.getRankNumber(rankupUtils.getCurrentRank(player, ranksList), ranksList));

                {
                    if (Main.rankupModule) {
                        if (Objects.equals(rankupUtils.getCurrentRank(player, ranksList), "rank.netherstar")) {
                            sender.sendMessage(getRankMaxMessage());
                        } else {
                            if (player.getInventory().containsAtLeast(shopUpgrade, itemCostRank)) {
                                rankupUtils.setRankPlayer(player, ranksList);
                                rankupUtils.setRankTag(player, ranksList, rankupUtils.listRanksTags());
                                Bukkit.broadcastMessage(anuncioRank);
                                player.getInventory().removeItem(shopUpgrade);

                            } else {
                                sender.sendMessage(getErroMessage(itemCostRank));
                            }
                        }
                    } else {
                        sender.sendMessage(getCommandOff());
                    }
                }
            } else {
                sender.sendMessage(getConsoleMessage());
            }
            return true;
        }
    }
}