package us.smartmc.gamescore.instance.game;

import lombok.Getter;
import us.smartmc.gamescore.event.game.*;
import us.smartmc.gamescore.event.player.GamePlayerGameJoinEvent;
import us.smartmc.gamescore.event.player.GamePlayerQuitEvent;
import us.smartmc.gamescore.instance.player.GameCorePlayer;
import us.smartmc.gamescore.instance.player.PlayerStatus;
import us.smartmc.gamescore.instance.timer.CountdownTimer;
import us.smartmc.gamescore.manager.GameSessionTeamsManager;
import us.smartmc.gamescore.util.BukkitUtil;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public abstract class Game implements IGame {

    // Identifier for the game session instance (4 the future we can use that for save histories, last games, etc...)
    @Getter
    private final UUID sessionId = UUID.randomUUID();

    protected GameStatus status = GameStatus.WAITING;

    @Getter
    protected CountdownTimer startTimer, endTimer;

    @Getter
    protected final Set<GameCorePlayer> players = new HashSet<>();

    private final GameSessionTeamsManager teamsManager = new GameSessionTeamsManager();

    @Override
    public void start() {
        BukkitUtil.callEvent(new GamePreStartEvent(this));

        // Perform after 0,5s for call post start event
        BukkitUtil.runLater(() -> {
            BukkitUtil.callEvent(new GamePostStartEvent(this));
        }, 10);
    }

    @Override
    public void end() {
        BukkitUtil.callEvent(new GamePreEndEvent(this));

        // Perform after 0,5s for call post end event
        BukkitUtil.runLater(() -> {
            BukkitUtil.callEvent(new GamePostEndEvent(this));
        }, 10);
    }

    @Override
    public void joinPlayer(GameCorePlayer player) {
        players.add(player);
        player.setCurrentGame(this);
        player.setStatus(PlayerStatus.PRE_GAME);
        BukkitUtil.callEvent(new GamePlayerGameJoinEvent(player));
    }

    @Override
    public void joinSpectatorViewer(GameCorePlayer player) {
        player.setStatus(PlayerStatus.SPECTATOR_VIEWER);
    }

    @Override
    public void leavePlayer(GameCorePlayer player) {
        players.remove(player);
        teamsManager.remove(player.getUUID());
        player.setCurrentGame(null);
        BukkitUtil.callEvent(new GamePlayerQuitEvent(player));
    }

    @Override
    public void killPlayer(GameCorePlayer player) {
        player.setStatus(PlayerStatus.SPECTATOR_DEATH);
    }

    @Override
    public Set<GameCorePlayer> getPlayersByStatus(PlayerStatus status) {
        return getPlayers().stream().filter(player -> player.getStatus().equals(status)).collect(Collectors.toSet());
    }

    @Override
    public void setStatus(GameStatus status) {
        GameStatus previousStatus = this.status;
        this.status = status;
        BukkitUtil.callEvent(new GameStatusChangeEvent(this, previousStatus, status));
    }

    @Override
    public GameStatus getStatus() {
        return status;
    }

    @Override
    public GameSessionTeamsManager getGameSessionTeamsManager() {
        return teamsManager;
    }
}
