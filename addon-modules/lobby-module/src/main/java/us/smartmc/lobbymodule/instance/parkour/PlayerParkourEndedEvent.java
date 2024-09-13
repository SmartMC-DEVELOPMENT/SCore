package us.smartmc.lobbymodule.instance.parkour;

import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

@Getter
public class PlayerParkourEndedEvent extends Event {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final Player player;
    private final PlayerParkourSession session;

    public PlayerParkourEndedEvent(PlayerParkourSession session) {
        this.player = session.getPlayer();
        this.session = session;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }
}
