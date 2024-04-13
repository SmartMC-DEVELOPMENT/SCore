package us.smartmc.gamesmanager.gamesmanagerspigot.instance.event;

import lombok.Getter;
import us.smartmc.gamesmanager.gamesmanagerspigot.instance.event.GameEvent;
import us.smartmc.gamesmanager.gamesmanagerspigot.instance.game.GameInstance;
import us.smartmc.gamesmanager.gamesmanagerspigot.instance.player.GamePlayer;

@Getter
public abstract class GamePlayerEvent extends GameEvent {

    private final GamePlayer gamePlayer;

    public GamePlayerEvent(GamePlayer instance) {
        this.gamePlayer = instance;
    }
}
