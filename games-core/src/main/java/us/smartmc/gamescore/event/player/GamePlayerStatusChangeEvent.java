package us.smartmc.gamescore.event.player;

import lombok.Getter;
import org.bukkit.entity.Player;
import us.smartmc.gamescore.instance.event.GameCorePlayerEvent;
import us.smartmc.gamescore.instance.player.PlayerStatus;

@Getter
public class GamePlayerStatusChangeEvent extends GameCorePlayerEvent {

    private final PlayerStatus previousStatus, currentStatus;

    public GamePlayerStatusChangeEvent(Player player, PlayerStatus previousStatus, PlayerStatus currentStatus) {
        super(player);
        this.previousStatus = previousStatus;
        this.currentStatus = currentStatus;
    }
}
