package com.totalcraft.soled.Listeners;

import com.totalcraft.soled.Utils.GriefPreventionUtils;
import com.totalcraft.soled.Utils.Utils;
import me.ryanhamshire.GriefPrevention.Claim;
import me.ryanhamshire.GriefPrevention.events.ClaimResizeEvent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;

import static com.totalcraft.soled.Utils.GriefPreventionUtils.getDifferenceBlocks;
import static com.totalcraft.soled.Utils.PrefixMsgs.getPmTTC;

public class ClaimResize implements Listener {
    private final Plugin plugin;

    public ClaimResize(Plugin plugin) {
        this.plugin = plugin;
    }

    public static HashMap<String, Boolean> resizeClaim = new HashMap<>();
    public static HashMap<String, String> claimSave = new HashMap<>();
    @EventHandler
    public void onClaimResizeEvent(ClaimResizeEvent event) {
        Claim oldClaim = event.getOldClaim();
        Claim newClaim = event.getNewClaim();
        Location newClaimGreater = newClaim.getGreaterBoundaryCorner();
        Location newClaimLesser = newClaim.getLesserBoundaryCorner();
        String XZ = "Loc: " +  newClaimGreater.getX() + newClaimGreater.getBlockZ() + newClaimLesser.getBlockX() + newClaimLesser.getBlockZ();
        Player player = Bukkit.getPlayer(oldClaim.getOwnerName());
        GriefPreventionUtils griefPreventionUtils = new GriefPreventionUtils(plugin);

        if (!Utils.getAdm(player)) {
            return;
        }
        if (!resizeClaim.containsKey(player.getName())) {
            resizeClaim.put(player.getName(), false);
        }
        if (resizeClaim.get(player.getName())) {
            if (!XZ.equals(claimSave.get(player.getName()))) {
                event.setCancelled(true);
                player.sendMessage(getPmTTC("&cVocê não pode redimensionar o terreno com o tamanho diferente da verificação. Refaça a verifcação"));
                resizeClaim.put(player.getName(), false);
            } else {
                resizeClaim.put(player.getName(), false);
            }
        } else {
            event.setCancelled(true);
            player.sendMessage("\n" + getPmTTC("&cVerificando blocos do terreno. Aguarde" + "\n "));
            int delay1 = griefPreventionUtils.claimContainsBlock(player, oldClaim, player.getWorld(), false, false, false);
            Bukkit.getScheduler().runTaskLater(plugin, () -> {
                int blocks1 = getDifferenceBlocks.get(player.getName());
                int delay2 = griefPreventionUtils.claimContainsBlock(player, newClaim, player.getWorld(), false, false, false);
                Bukkit.getScheduler().runTaskLater(plugin, () -> {
                    int blocks2 = getDifferenceBlocks.get(player.getName());
                    if (blocks1 == blocks2) {
                        resizeClaim.put(player.getName(), true);
                        claimSave.put(player.getName(), XZ);
                        player.sendMessage(getPmTTC("&bVerificado, Você pode redimensionar o terreno no mesmo lugar que selecionou"));
                    } else {
                        player.sendMessage(getPmTTC("&cHá blocos de limite que não tem nessa nova seleção." +
                                "\n&bDigite /blocklimit dentro do Terreno para saber a localização dos blocos limitados"));
                    }
                }, 15L * delay2);
            }, 15L * delay1);
        }
    }
}
