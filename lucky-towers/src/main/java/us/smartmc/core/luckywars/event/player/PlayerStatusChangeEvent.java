package us.smartmc.core.luckywars.event.player;

import lombok.Getter;
import org.bukkit.event.Cancellable;
import us.smartmc.core.luckywars.event.GamePlayerEvent;
import us.smartmc.core.luckywars.instance.player.GamePlayer;
import us.smartmc.core.luckywars.instance.player.PlayerStatus;

@Getter
public class PlayerStatusChangeEvent extends GamePlayerEvent implements Cancellable {

    private final PlayerStatus status, earlyStatus;
    private boolean cancelled;

    public PlayerStatusChangeEvent(GamePlayer gamePlayer, PlayerStatus newStatus, PlayerStatus earlyStatus) {
        super(gamePlayer);
        this.status = newStatus;
        this.earlyStatus = earlyStatus;
        this.cancelled = newStatus.equals(earlyStatus);
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean b) {
        cancelled = b;
    }
}
