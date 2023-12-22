package us.smartmc.lobbymodule.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import us.smartmc.core.pluginsapi.spigot.event.PlayerDataLoadedEvent;
import us.smartmc.core.pluginsapi.spigot.event.PlayerLanguageChangedEvent;
import us.smartmc.core.pluginsapi.spigot.player.CorePlayer;
import us.smartmc.core.pluginsapi.util.SyncUtil;
import us.smartmc.lobbymodule.menu.JoinItemMenu;
import us.smartmc.smartaddons.plugin.AddonListener;

public class JoinItemListener extends AddonListener implements Listener {

    @EventHandler(priority = EventPriority.MONITOR)
    public void onJoin(PlayerDataLoadedEvent event) {
        if (event.getPlayer() == null) return;
        if (!isEnabled()) return;
        SyncUtil.sync(() -> giveMenu(event.getCorePlayer()));
    }

    @EventHandler
    public void onLanguageChange(PlayerLanguageChangedEvent event) {
        if (!isEnabled()) return;
        giveMenu(event.getCorePlayer());
    }

    private static void giveMenu(CorePlayer cPlayer) {
        Player player = cPlayer.get();
        if (player == null) return;
        cPlayer.clearInventory();
        JoinItemMenu menu = new JoinItemMenu(player);
        menu.set(player);
    }

}
