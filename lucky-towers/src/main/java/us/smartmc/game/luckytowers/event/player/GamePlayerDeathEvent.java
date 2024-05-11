package us.smartmc.game.luckytowers.event.player;

import lombok.Getter;
import org.bukkit.event.Cancellable;
import us.smartmc.game.luckytowers.event.GamePlayerEvent;
import us.smartmc.game.luckytowers.instance.player.GamePlayer;
import us.smartmc.game.luckytowers.instance.player.PlayerStatus;

@Getter
public class GamePlayerDeathEvent extends GamePlayerEvent {

    public GamePlayerDeathEvent(GamePlayer gamePlayer) {
        super(gamePlayer);
    }
}
