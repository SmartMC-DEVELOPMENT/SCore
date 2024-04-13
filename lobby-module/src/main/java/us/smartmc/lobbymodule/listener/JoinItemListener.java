package us.smartmc.lobbymodule.listener;

import me.imsergioh.pluginsapi.event.PlayerDataLoadedEvent;
import me.imsergioh.pluginsapi.event.PlayerLanguageChangedEvent;
import me.imsergioh.pluginsapi.instance.player.CorePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;
import us.smartmc.core.SmartCore;
import us.smartmc.lobbymodule.handler.VisibilityManager;
import us.smartmc.lobbymodule.instance.PlayerVisibility;
import us.smartmc.lobbymodule.menu.JoinItemMenu;
import us.smartmc.smartaddons.plugin.AddonListener;

public class JoinItemListener extends AddonListener implements Listener {

    @EventHandler(priority = EventPriority.MONITOR)
    public void onJoin(PlayerDataLoadedEvent event) {
        if (!isEnabled()) return;
        if (event.getPlayer() == null) return;

        giveMenu(event.getCorePlayer());
    }

    @EventHandler
    public void onLanguageChange(PlayerLanguageChangedEvent event) {
        if (!isEnabled()) return;
        giveMenu(event.getCorePlayer());
    }

    private static void giveMenu(CorePlayer cPlayer) {
        Player player = cPlayer.get();
        if (player == null) {
            return;
        }
        cPlayer.clearInventory();
        JoinItemMenu menu = new JoinItemMenu(player);
        menu.set(player);
    }
}
