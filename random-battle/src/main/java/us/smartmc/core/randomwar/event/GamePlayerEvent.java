package us.smartmc.core.randomwar.event;

import lombok.Getter;
import org.bukkit.entity.Player;
import us.smartmc.core.randomwar.instance.game.GameSession;
import us.smartmc.core.randomwar.instance.player.GamePlayer;

@Getter
public class GamePlayerEvent extends GamePluginEvent {

    private final GamePlayer gamePlayer;

    public GamePlayerEvent(GamePlayer gamePlayer) {
        this.gamePlayer = gamePlayer;
    }

    public GameSession getSession() {
        return gamePlayer.getGameSession();
    }

    public Player getPlayer() {
        return gamePlayer.getBukkitPlayer();
    }

}
