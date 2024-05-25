package us.smartmc.game.luckytowers.event.player;

import lombok.Getter;
import us.smartmc.game.luckytowers.event.GamePlayerEvent;
import us.smartmc.game.luckytowers.instance.player.GamePlayer;

@Getter
public class GamePlayerWinEvent extends GamePlayerEvent {

    public GamePlayerWinEvent(GamePlayer gamePlayer) {
        super(gamePlayer);
    }
}
