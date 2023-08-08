package com.totalcraft.soled.Commands;

import com.harley.totalutilities.common.limits.BlockData;
import com.harley.totalutilities.common.limits.LimitControl;
import com.harley.totalutilities.common.limits.PlayerData;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static com.totalcraft.soled.Utils.PrefixMsgs.*;

public class Quarry implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(getPmConsole());
            return true;
        }
        Player player = (Player) sender;
        limitQuarry(player);
        return true;
    }
    private void limitQuarry(Player player) {
        boolean msg = false;
        StringBuilder sb = new StringBuilder();
        PlayerData playerData = LimitControl.getPlayerData(player.getName(), false);
        if (playerData.blocks != null && playerData.blocks.containsKey("1503:0")) {
            int limit = (int) playerData.blocks.get("1503:0");
            if (limit > 0) {
                for (Object test : LimitControl.blockDataMap.values()) {
                    BlockData blockData = (BlockData) test;
                    if (blockData.getBlockId() == 1503 && player.getName().equals(blockData.getOwner())) {
                        msg = true;
                        sb.append("\n").append(getFormatColor("&6Mundo:&f " + player.getWorld().getName() + " &6X:&f " + blockData.getX() + " &6Y:&f " + blockData.getY() + " &6Z:&f " + blockData.getZ()));
                    }
                }
            }
        }
        if (msg) {
            player.sendMessage(getPmTTC("&bQuarry colocadas:"));
            player.sendMessage(sb.toString());
        } else {
            player.sendMessage(getPmTTC("&cVocê não tem quarry colocadas no mundo"));
        }
    }
}
