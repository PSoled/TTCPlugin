package com.totalcraft.soled.Listeners;

import com.totalcraft.soled.Utils.RankupUtils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import ru.tehkode.permissions.PermissionUser;
import ru.tehkode.permissions.events.PermissionEntityEvent;

import static com.totalcraft.soled.Configs.MainConfig.eventGroupChangeModule;

public class GroupChange implements Listener {
    RankupUtils rankupUtils = new RankupUtils();
    @EventHandler
    public void onGroupChange(PermissionEntityEvent event) {
        if (eventGroupChangeModule) {
            if (event.getEntity() instanceof PermissionUser) {
                String playerName = event.getEntity().getName();
                rankupUtils.eventSetRank(playerName);
            }
        }
    }
}
