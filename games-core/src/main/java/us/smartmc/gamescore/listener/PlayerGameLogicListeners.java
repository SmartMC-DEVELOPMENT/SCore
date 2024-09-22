package us.smartmc.gamescore.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import us.smartmc.gamescore.api.GamesCoreAPI;
import us.smartmc.gamescore.event.player.GamePlayerQuitEvent;

public class PlayerGameLogicListeners implements Listener {

    @EventHandler
    public void onPlayerQuit(GamePlayerQuitEvent event) {
        GamesCoreAPI.getGamesManager().unregisterPlayer(event.getBukkitPlayer());
    }
}
