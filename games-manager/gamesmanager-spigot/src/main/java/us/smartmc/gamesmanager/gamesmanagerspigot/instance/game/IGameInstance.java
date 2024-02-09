package us.smartmc.gamesmanager.gamesmanagerspigot.instance.game;

import us.smartmc.gamesmanager.gamesmanagerspigot.instance.player.GamePlayer;
import us.smartmc.gamesmanager.gamesmanagerspigot.manager.GameManager;

import java.util.Collection;

public interface IGameInstance {

    void load();
    void unload();

    Collection<GamePlayer> getAlivePlayers();
    Collection<GamePlayer> getPlayers();

    GameStatus getStatus();
    String getName();
    GameManager<?> getManager();
}
