package com.totalcraft.soled.Commands;

import com.totalcraft.soled.Utils.Utils;
import me.ryanhamshire.GriefPrevention.Claim;
import me.ryanhamshire.GriefPrevention.GriefPrevention;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static com.totalcraft.soled.Utils.GriefPreventionUtils.sendLimitsInClaim;
import static com.totalcraft.soled.Utils.PrefixMsgs.getPmConsole;
import static com.totalcraft.soled.Utils.PrefixMsgs.getPmTTC;

public class    BlockLimit implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(getPmConsole());
            return true;
        }
        Player player = (Player) sender;
        Claim claim = GriefPrevention.instance.dataStore.getClaimAt(player.getLocation(), false, null);
        if (claim == null) {
            player.sendMessage(getPmTTC("&cVocê não está em um terreno"));
            return true;
        }
        if (!claim.getOwnerName().equalsIgnoreCase(player.getName()) && Utils.getAdm(sender)) {
            player.sendMessage(getPmTTC("&cVocê não é dono deste terreno"));
            return true;
        }
        sendLimitsInClaim(player, claim);
        return true;
    }
}
