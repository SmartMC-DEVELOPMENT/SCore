package us.smartmc.gamesmanager.game;

import us.smartmc.gamesmanager.player.GamePlayer;

import java.util.UUID;
import java.util.function.Consumer;

@SuppressWarnings("unused")
public interface IGameSession {

    void unload();

    void start();
    void end();

    void  joinPlayer(GamePlayer player);
    void quitPlayer(GamePlayer player);

    void deathPlayer(GamePlayer player);

    void checkStart();

    boolean canStart();
    boolean canPlayerJoin(GamePlayer player);
    boolean canSpectatorJoin(GamePlayer player);

    void broadcast(String message, Object... args);

    <T extends GamePlayer> void forEachGamePlayer(Class<T> type, Consumer<T> consumer);

    void setStatus(GameStatus status);
    GameStatus getStatus();

    UUID getID();

}
