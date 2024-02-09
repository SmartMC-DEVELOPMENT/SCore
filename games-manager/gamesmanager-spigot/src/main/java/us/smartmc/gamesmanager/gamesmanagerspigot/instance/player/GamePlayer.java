package us.smartmc.gamesmanager.gamesmanagerspigot.instance.player;

import lombok.Getter;
import lombok.Setter;
import us.smartmc.gamesmanager.gamesmanagerspigot.instance.game.GameInstance;
import us.smartmc.gamesmanager.gamesmanagerspigot.manager.GamePlayerManager;

import java.util.UUID;

@Getter
public abstract class GamePlayer extends OfflineGamePlayer {

    @Setter
    private GameInstance gameInstance;

    public GamePlayer(GamePlayerManager<?> manager, UUID uuid) {
        super(manager, uuid);
    }
}
