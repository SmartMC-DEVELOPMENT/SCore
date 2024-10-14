package us.smartmc.lobbymodule.listener;

import me.imsergioh.pluginsapi.event.PlayerDataLoadedEvent;
import me.imsergioh.pluginsapi.event.PlayerLanguageChangedEvent;
import me.imsergioh.pluginsapi.instance.menu.GUIMenu;
import me.imsergioh.pluginsapi.instance.player.CorePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
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
        player.getInventory().clear();
        GUIMenu.unregisterSetGUI(player.getUniqueId());
        JoinItemMenu menu = new JoinItemMenu(player);
        menu.set(player);
    }
}
