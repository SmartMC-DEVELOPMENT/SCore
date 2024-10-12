package us.smartmc.gamescore.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import us.smartmc.gamescore.util.BukkitUtil;
import us.smartmc.gamescore.util.EditorModeUtil;

public class AdminManagerListeners implements Listener {

    @EventHandler
    public void quitEditorMode(PlayerQuitEvent event) {
        EditorModeUtil.leaveEditorMode(event.getPlayer());
    }

    @EventHandler
    public void setFlyingAtEditorMode(PlayerGameModeChangeEvent event) {
        BukkitUtil.runLater(() -> {
            Player player = event.getPlayer();
            if (!EditorModeUtil.isInEditorMode(player)) return;
            player.setAllowFlight(true);
            player.setFlying(true);
        }, 1);
    }
}
