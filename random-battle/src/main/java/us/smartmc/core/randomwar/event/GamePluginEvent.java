package us.smartmc.core.randomwar.event;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class GamePluginEvent extends Event {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    public GamePluginEvent() {
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }
}
