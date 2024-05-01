package us.smartmc.gamesmanager.gamesmanagerspigot.instance.event.player;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.event.Cancellable;
import us.smartmc.gamesmanager.gamesmanagerspigot.instance.event.GamePlayerEvent;
import us.smartmc.gamesmanager.gamesmanagerspigot.instance.player.GamePlayer;
import us.smartmc.gamesmanager.gamesmanagerspigot.instance.player.PlayerStatus;

@Getter
public class GamePlayerStatusChangeEvent extends GamePlayerEvent implements Cancellable {

    @Setter
    private boolean cancelled;
    private final PlayerStatus previous, status;

    public GamePlayerStatusChangeEvent(GamePlayer instance, PlayerStatus previous, PlayerStatus status) {
        super(instance);
        this.previous = previous;
        this.status = status;
    }
}
