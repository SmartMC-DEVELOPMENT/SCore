package us.smartmc.gamescore.instance.game;

import us.smartmc.gamescore.instance.player.GameCorePlayer;
import us.smartmc.gamescore.instance.player.PlayerStatus;
import us.smartmc.gamescore.instance.timer.CountdownTimer;
import us.smartmc.gamescore.manager.team.GameSessionTeamsManager;
import us.smartmc.gamescore.manager.GamesManager;

import java.util.Set;
import java.util.UUID;

public interface IGame {

    UUID getSessionId();

    CountdownTimer getStartTimer();
    CountdownTimer getEndTimer();

    default void startWithCountdown() {
        if (getStartTimer() == null) {
            start();
            return;
        }
        getStartTimer().start();
    }

    default void endWithCountdown() {
        if (getEndTimer() == null) {
            end();
            return;
        }
        getEndTimer().start();
    }

    void start();
    void end();

    void setStatus(GameStatus status);
    GameStatus getStatus();

    void joinPlayer(GameCorePlayer player);
    void joinSpectatorViewer(GameCorePlayer player);

    void killPlayer(GameCorePlayer player);

    void leavePlayer(GameCorePlayer player);

    default Set<GameCorePlayer> getSpectators() {
        Set<GameCorePlayer> players = getPlayersByStatus(PlayerStatus.SPECTATOR_VIEWER);
        players.addAll(getPlayersByStatus(PlayerStatus.SPECTATOR_DEATH));
        return players;
    }

    Set<GameCorePlayer> getPlayersByStatus(PlayerStatus status);
    Set<GameCorePlayer> getPlayers();

    GameSessionTeamsManager getGameSessionTeamsManager();

    default GamesManager getManager() {
        return GamesManager.getManager(GamesManager.class);
    }

}
