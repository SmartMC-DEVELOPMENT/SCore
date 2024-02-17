package us.smartmc.gamesmanager.gamesmanagerspigot.instance.game;

import us.smartmc.gamesmanager.gamesmanagerspigot.instance.player.GamePlayer;
import us.smartmc.gamesmanager.gamesmanagerspigot.manager.GameManager;

import java.util.Collection;
import java.util.Map;
import java.util.UUID;

public interface IGameInstance {

    void load();
    void unload();

    void joinPlayer(GamePlayer player);
    void quitPlayer(GamePlayer player);
    boolean canJoinPlayer(GamePlayer player);
    Collection<GamePlayer> getAlivePlayers();
    Map<UUID, GamePlayer> getPlayers();

    boolean canGameStart();

    GameMap getMap();
    GameStatus getStatus();
    String getName();
    GameManager<?> getManager();
}
