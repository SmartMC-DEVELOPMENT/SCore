package us.smartmc.game.luckytowers.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import us.smartmc.game.luckytowers.config.MainPluginConfig;
import us.smartmc.game.luckytowers.event.player.PlayerStatusChangeEvent;
import us.smartmc.game.luckytowers.instance.player.PlayerStatus;

public class PlayerLogicListeners implements Listener {

    @EventHandler
    public void teleportToLobby(PlayerStatusChangeEvent event) {
        if (event.isCancelled()) return;
        if (!event.getStatus().equals(PlayerStatus.LOBBY) && !MainPluginConfig.isLobbyEnabled()) return;

        // Teleport to lobby if gameplayer is online (Should xd)
        event.getGamePlayer().onlinePlayer(player -> player.teleport(MainPluginConfig.getLobby()));
    }
}
