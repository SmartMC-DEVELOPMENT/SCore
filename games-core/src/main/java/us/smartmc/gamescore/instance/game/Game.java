package us.smartmc.gamescore.instance.game;

import lombok.Getter;
import org.bukkit.entity.Player;
import us.smartmc.gamescore.event.GameStatusChangeEvent;
import us.smartmc.gamescore.instance.player.GameCorePlayer;
import us.smartmc.gamescore.instance.player.PlayerStatus;
import us.smartmc.gamescore.util.BukkitUtil;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public abstract class Game implements IGame {

    protected GameStatus status = GameStatus.WAITING;

    @Getter
    protected final Set<Player> players = new HashSet<>();

    @Override
    public void joinPlayer(Player player) {
        players.add(player);
        GameCorePlayer.of(player).setStatus(PlayerStatus.PRE_GAME);
    }

    @Override
    public void joinSpectatorViewer(Player player) {
        GameCorePlayer.of(player).setStatus(PlayerStatus.SPECTATOR_VIEWER);
    }

    @Override
    public void leavePlayer(Player player) {
        players.remove(player);
    }

    @Override
    public void killPlayer(Player player) {
        GameCorePlayer.of(player).setStatus(PlayerStatus.SPECTATOR_DEATH);
    }

    @Override
    public Set<Player> getPlayersByStatus(PlayerStatus status) {
        return getPlayers().stream().filter(player -> {
            GameCorePlayer gameCorePlayer = GameCorePlayer.of(player);
            return gameCorePlayer.getStatus().equals(status);
        }).collect(Collectors.toSet());
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
}
