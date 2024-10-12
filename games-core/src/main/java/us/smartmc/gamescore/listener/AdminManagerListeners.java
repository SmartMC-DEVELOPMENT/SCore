package us.smartmc.gamescore.listener;

import me.imsergioh.pluginsapi.instance.menu.GUIMenu;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import us.smartmc.gamescore.menu.EditMapInventoryMenu;

public class AdminManagerListeners implements Listener {

    @EventHandler
    public void quitEditorMode(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        GUIMenu menu = GUIMenu.getSetGUI(player);
        if (menu instanceof EditMapInventoryMenu inv) {
            inv.leave(player);
            GUIMenu.unregisterSetGUI(player.getUniqueId());
        }
    }
}
