package us.smartmc.lobbymodule.listener;

import me.imsergioh.pluginsapi.event.PlayerDataLoadedEvent;
import me.imsergioh.pluginsapi.event.PlayerLanguageChangedEvent;
import me.imsergioh.pluginsapi.instance.player.CorePlayer;
import me.imsergioh.pluginsapi.util.SyncUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
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
