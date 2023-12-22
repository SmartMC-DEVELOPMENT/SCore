package us.smartmc.core.pluginsapi.spigot.event;

import us.smartmc.core.pluginsapi.spigot.player.CorePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class FirstCorePlayerJoinEvent extends Event {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final CorePlayer corePlayer;

    public FirstCorePlayerJoinEvent(CorePlayer corePlayer) {
        this.corePlayer = corePlayer;
    }

    public Player getPlayer() {
        return corePlayer.get();
    }

    public CorePlayer getCorePlayer() {
        return corePlayer;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }
}
