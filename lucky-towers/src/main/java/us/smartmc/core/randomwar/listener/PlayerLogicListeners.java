package us.smartmc.core.randomwar.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import us.smartmc.core.randomwar.config.MainPluginConfig;
import us.smartmc.core.randomwar.event.player.PlayerStatusChangeEvent;
import us.smartmc.core.randomwar.instance.player.PlayerStatus;

public class PlayerLogicListeners implements Listener {

    @EventHandler
    public void teleportToLobby(PlayerStatusChangeEvent event) {
        if (event.isCancelled()) return;
        if (!event.getStatus().equals(PlayerStatus.LOBBY) && !MainPluginConfig.isLobbyEnabled()) return;

        // Teleport to lobby if gameplayer is online (Should xd)
        event.getGamePlayer().onlinePlayer(player -> player.teleport(MainPluginConfig.getLobby()));
    }
}
