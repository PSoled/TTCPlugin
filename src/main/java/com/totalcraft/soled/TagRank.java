package com.totalcraft.soled;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import ru.tehkode.permissions.PermissionUser;
import ru.tehkode.permissions.bukkit.PermissionsEx;

import java.util.List;

import static com.totalcraft.soled.prefixMsgs.getPmConsole;
import static com.totalcraft.soled.prefixMsgs.getPmTTC;

public class TagRank implements CommandExecutor {
    RankupUtils rankupUtils = new RankupUtils();
    List<String> ranksList = rankupUtils.ranksList;

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(getPmConsole());
        }
        assert sender instanceof Player;
        Player player = (Player) sender;
        PermissionUser user = PermissionsEx.getUser(player);
        boolean hasRank = false;

        if (cmd.getName().equalsIgnoreCase("tagrank") && args.length == 0) {
            player.sendMessage(getPmTTC("&eUse /tagrank &aon&e/&coff"));
            return true;
        }

        for (String rank : ranksList) {
            if (user.has(rank)) {
                hasRank = true;
                if (cmd.getName().equalsIgnoreCase("tagrank") && args.length > 0) {
                    if (args[0].equalsIgnoreCase("on")) {
                        rankupUtils.setRankTag(player, ranksList, rankupUtils.listRanksTags());
                        player.sendMessage(getPmTTC("Suas tag de Rank foi &aAtivada"));
                    } else if (args[0].equalsIgnoreCase("off")) {
                        user.setSuffix("", "");
                        player.sendMessage(getPmTTC("Suas tag de Rank foi &cDesativada"));
                    }
                }
            }
        }
        if (!hasRank) {
            player.sendMessage(getPmTTC("&cQuer usar este comando sem ter rank ?"));
        }
        return true;
    }

}
