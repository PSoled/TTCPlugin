package com.totalcraft.soled.Commands;

import com.totalcraft.soled.Configs.BlockProtectData;
import com.totalcraft.soled.Utils.Utils;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import ru.tehkode.permissions.PermissionUser;
import ru.tehkode.permissions.bukkit.PermissionsEx;

import java.util.Iterator;
import java.util.Objects;

import static com.totalcraft.soled.Configs.BlockProtectData.blockConfig;
import static com.totalcraft.soled.Utils.PrefixMsgs.*;

public class BlockProtect implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("blockprotect")) {
            switch (args.length > 0 ? args[0].toLowerCase() : "") {
                case "localizar":
                case "loc":
                    break;
                default:
                    sender.sendMessage(getCommandBP(sender));
                    break;
            }

            if (args.length > 0 && args[0].equalsIgnoreCase("localizar")) {
                if (!(sender instanceof Player)) {
                    sender.sendMessage(getPmConsole());
                    return true;
                }
                Player player = (Player) sender;
                if (!Objects.equals(BlockProtectData.getLocBlock(player.getName()), "null")) {
                    player.sendMessage(getPmTTC("&eLista da localização do seus blocos protegidos\n"));
                    player.sendMessage(ChatColor.AQUA + BlockProtectData.getLocBlock(player.getName()));
                    return true;
                }
                player.sendMessage(getPmTTC("&cVocê não tem blocos protegidos"));
            }

            if (args.length > 0 && args[0].equalsIgnoreCase("loc")) {
                if (sender instanceof Player) {
                    if (Utils.getAdm(sender)) {
                        sender.sendMessage(getPmNotAdm());
                        return true;
                    }
                }
                if (args.length != 2) {
                    sender.sendMessage(getPmTTC("&cUse: /blockprotect loc <Player>"));
                    return true;
                }
                String playerName = args[1];
                if (!Objects.equals(BlockProtectData.getLocBlock(playerName), "null")) {
                    sender.sendMessage(getPmTTC("&eLista da localização dos blocos protegidos do " + playerName + "\n"));
                    sender.sendMessage(ChatColor.AQUA + BlockProtectData.getLocBlock(playerName));
                    return true;
                }
                sender.sendMessage(getPmTTC("&cEste player não tem blocos protegidos"));
            }
        }
        return true;
    }

    public static boolean blockProtectBreak(Player player, Location blockLocation) {
        PermissionUser user = PermissionsEx.getUser(player);
        Iterator<Location> it = BlockProtectData.protectedBlock.keySet().iterator();
        while (it.hasNext()) {
            Location loc = it.next();
            String owner = BlockProtectData.protectedBlock.get(loc);
            if (blockLocation.distance(loc) <= 0 && player.getName().equals(owner)) {
                player.sendMessage(getPmTTC("&cVocê retirou um bloco protegido"));
                blockConfig.set("protected-blocks." + loc.getWorld().getName() + "." + loc.getBlockX() + "." + loc.getBlockY() + "." + loc.getBlockZ(), null);
                it.remove();
                BlockProtectData.saveProtectedBlocks();
                break;
            }
            if (blockLocation.distance(loc) <= 0 && (user.has("ttcsoled.admin") || player.isOp())) {
                player.sendMessage(getPmTTC("&cVocê removeu um bloco protegido do player &f" + BlockProtectData.protectedBlock.get(loc)));
                blockConfig.set("protected-blocks." + loc.getWorld().getName() + "." + loc.getBlockX() + "." + loc.getBlockY() + "." + loc.getBlockZ(), null);
                it.remove();
                BlockProtectData.saveProtectedBlocks();
                break;
            }
            if (blockLocation.distance(loc) <= 3 && (user.has("ttcsoled.admin") || player.isOp())) {
                break;
            }
            if (blockLocation.distance(loc) <= 0 && !player.getName().equals(owner)) {
                player.sendMessage(getPmTTC("&cBloco protegido por &f" + owner));
                return true;
            }
            if (blockLocation.distance(loc) <= 3 && !player.getName().equals(owner)) {
                player.sendMessage(getPmTTC("&cHá um bloco protegido por perto"));
                return true;
            }
        }
        return false;
    }
    static boolean blockCancelled = false;
    public static boolean blockProtectPlace(Player player, Block block, Location blockLocation) {
        if (block.getLocation().getWorld().getName().equals("minerar")) {
            PermissionUser user = PermissionsEx.getUser(player);
            if (!user.has("ttcsoled.admin") && !player.isOp()) {
                for (Location loc : BlockProtectData.protectedBlock.keySet()) {
                    String owner = BlockProtectData.protectedBlock.get(loc);
                    if (block.getTypeId() == 1503 && !player.getName().equals(owner)
                            && Math.abs(blockLocation.getBlockX() - loc.getBlockX()) <= 30
                            && Math.abs(blockLocation.getBlockZ() - loc.getBlockZ()) <= 30) {
                        player.sendMessage(getPmTTC("&cHá blocos protegido por perto, Se afaste para colocar a sua Pedreira"));
                        blockCancelled = true;
                        return true;
                    }
                    if (blockLocation.distance(loc) <= 5 && !player.getName().equals(owner)) {
                        player.sendMessage(getPmTTC("&cVocê não pode colocar blocos perto de um bloco protegido"));
                        blockCancelled = true;
                        return true;
                    }
                }
                if (!blockCancelled) {
                    if (block.getTypeId() == 1503 || block.getTypeId() == 194 || block.getTypeId() == 195) {
                        player.sendMessage(getPmTTC("&eVocê colocou que é protegido no Mundo do Minerar"));
                        BlockProtectData.protectedBlock.put(block.getLocation(), player.getName());
                        BlockProtectData.saveProtectedBlocks();
                    }
                }
            }
        }
        return false;
    }
}

