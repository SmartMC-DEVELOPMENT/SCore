package us.smartmc.core.pluginsapi.spigot.event;

import us.smartmc.core.pluginsapi.spigot.player.CorePlayer;
import org.bukkit.entity.Player;

public class PlayerUnloadEvent extends CorePlayerEvent {

    public PlayerUnloadEvent(CorePlayer corePlayer) {
        super(corePlayer);
    }

    public Player getPlayer() {
        return getCorePlayer().get();
    }
}
