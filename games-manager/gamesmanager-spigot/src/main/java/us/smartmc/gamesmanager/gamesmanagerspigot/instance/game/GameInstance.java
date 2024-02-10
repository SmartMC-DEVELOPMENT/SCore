package us.smartmc.gamesmanager.gamesmanagerspigot.instance.game;

import lombok.Getter;
import us.smartmc.gamesmanager.gamesmanagerspigot.instance.event.game.GameMapChangeEvent;
import us.smartmc.gamesmanager.gamesmanagerspigot.instance.event.game.GameStatusChangeEvent;
import us.smartmc.gamesmanager.gamesmanagerspigot.instance.event.game.GameStatusChangedEvent;
import us.smartmc.gamesmanager.gamesmanagerspigot.instance.player.GamePlayer;
import us.smartmc.gamesmanager.gamesmanagerspigot.instance.player.PlayerStatus;
import us.smartmc.gamesmanager.gamesmanagerspigot.manager.GameManager;
import us.smartmc.gamesmanager.gamesmanagerspigot.util.BukkitUtil;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
public abstract class GameInstance implements IGameInstance {

    protected final GameManager<?> manager;
    protected final String name;
    protected GameStatus status = GameStatus.MAINTENANCE;
    protected GameMap map;

    private final Set<GamePlayer> players = new HashSet<>();

    public GameInstance(GameManager<?> manager, String name) {
        this.manager = manager;
        this.name = name;
    }

    public void setStatus(GameStatus status) {
        GameStatus previous = this.status;
        GameStatusChangeEvent event = new GameStatusChangeEvent(this, previous, status);
        BukkitUtil.callEvent(event);
        if (event.isCancelled()) return;
        this.status = status;
        BukkitUtil.callEvent(new GameStatusChangedEvent(this, previous, status));
    }

    public void setMap(GameMap map) {
        GameMap previous = this.map;
        if (previous != null) {
            GameMapChangeEvent event = new GameMapChangeEvent(this, previous, map);
            BukkitUtil.callEvent(event);
            if (event.isCancelled()) return;
            }
        this.map = map;
    }

    @Override
    public boolean canJoinPlayer(GamePlayer player) {
        if (status == GameStatus.MAINTENANCE) return false;
        boolean statusCanJoin = switch (status) {
            case STARTING, WAITING -> true;
            default -> false;
        };
        if (!statusCanJoin) return false;
        return getPlayers().size() >= map.getMaxPlayerSize();
    }

    @Override
    public Collection<GamePlayer> getAlivePlayers() {
        return getPlayers().stream().filter(p -> p.getStatus().equals(PlayerStatus.PLAYING)).collect(Collectors.toSet());
    }

    @Override
    public boolean canGameStart() {
        if (!status.equals(GameStatus.WAITING)) return false;
        return getPlayers().size() >= map.getMinPlayersSize();
    }
}
