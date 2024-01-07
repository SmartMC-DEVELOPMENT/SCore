package us.smartmc.gamesmanager.game;

import us.smartmc.gamesmanager.player.GamePlayer;

import java.util.UUID;
import java.util.function.Consumer;

@SuppressWarnings("unused")
public interface IGameSession<P extends GamePlayer> {

    void unload();

    void start();
    void end();

    void  joinPlayer(P player);
    void quitPlayer(P player);

    void deathPlayer(P player);

    void checkStart();

    boolean canStart();
    boolean canPlayerJoin(P player);
    boolean canSpectatorJoin(P player);

    void broadcast(String message, Object... args);

    void forEachGamePlayer(Class<? extends P> type, Consumer<P> consumer);

    void setStatus(GameStatus status);
    GameStatus getStatus();

    UUID getID();

}
