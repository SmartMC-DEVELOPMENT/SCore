package us.smartmc.gamescore.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import us.smartmc.gamescore.util.EditorModeUtil;

public class AdminManagerListeners implements Listener {

    @EventHandler
    public void quitEditorMode(PlayerQuitEvent event) {
        EditorModeUtil.leaveEditorMode(event.getPlayer());
    }
}
