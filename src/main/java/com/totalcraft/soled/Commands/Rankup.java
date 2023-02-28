package com.totalcraft.soled.Commands;

import com.totalcraft.soled.Configs.MainConfig;
import com.totalcraft.soled.Utils.RankupUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import ru.tehkode.permissions.PermissionUser;
import ru.tehkode.permissions.bukkit.PermissionsEx;

import java.util.List;
import java.util.Objects;

import static com.totalcraft.soled.Utils.PrefixMsgs.*;

public class Rankup implements CommandExecutor {
    RankupUtils rankupUtils = new RankupUtils();
    List<String> ranksList = rankupUtils.ranksList;

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            PermissionUser user = PermissionsEx.getUser(player);
            int itemCostRank = rankupUtils.listPriceitem().get(rankupUtils.getNextRank(player, ranksList));
            ItemStack shopUpgrade = new ItemStack(Material.getMaterial(4358), itemCostRank, (short) 0);
            String anuncioRank = getPmRankupRank(player.getName(), rankupUtils.getRankNumber(rankupUtils.getCurrentRank(player, ranksList), ranksList));
            {
                if (MainConfig.rankupModule) {
                    if (Objects.equals(rankupUtils.getCurrentRank(player, ranksList), "rankNetherstar")) {
                        sender.sendMessage(getPmRankMax());
                    } else {
                        if (Objects.equals(rankupUtils.getNextRank(player, ranksList), "rankNetherstar")) {
                            user.addPermission("totalessentials.commands.warp.netherstar");
                        }
                        if (Objects.equals(rankupUtils.getNextRank(player, ranksList), "rankFerro")) {
                            user.addPermission("totalessentials.commands.fly");
                        }
                        if (Objects.equals(rankupUtils.getNextRank(player, ranksList), "rankEsmeralda")) {
                            user.addPermission("totalessentials.bypass.teleport");
                        }
                        if (player.getInventory().containsAtLeast(shopUpgrade, itemCostRank)) {
                            rankupUtils.setRankPlayer(player, ranksList);
                            rankupUtils.setRankTag(player, ranksList, rankupUtils.listRanksTags());
                            Bukkit.broadcastMessage(anuncioRank);
                            player.getInventory().removeItem(shopUpgrade);

                        } else {
                            sender.sendMessage(getPmErro(itemCostRank));
                        }
                    }
                } else {
                    sender.sendMessage(getPmCommandOff());
                }
            }
        } else {
            sender.sendMessage(getPmConsole());
        }
        return true;
    }
}
