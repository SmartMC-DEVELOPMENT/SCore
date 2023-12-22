package us.smartmc.core.pluginsapi.spigot.event;

import us.smartmc.core.pluginsapi.spigot.player.CorePlayer;
import us.smartmc.core.pluginsapi.spigot.player.CorePlayerData;
import org.bukkit.entity.Player;

public class PlayerDataLoadedEvent extends CorePlayerEvent {

    private final CorePlayerData data;

    public PlayerDataLoadedEvent(CorePlayer corePlayer, CorePlayerData data) {
        super(corePlayer);
        this.data = data;
    }

    public Player getPlayer() {
        return getCorePlayer().get();
    }

    public CorePlayerData getData() {
        return data;
    }
}
