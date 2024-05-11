package us.smartmc.game.luckytowers.event.player;

import lombok.Getter;
import us.smartmc.game.luckytowers.event.GamePlayerEvent;
import us.smartmc.game.luckytowers.instance.player.GamePlayer;

@Getter
public class GamePlayerJoinSessionEvent extends GamePlayerEvent {

    public GamePlayerJoinSessionEvent(GamePlayer gamePlayer) {
        super(gamePlayer);
    }
}
