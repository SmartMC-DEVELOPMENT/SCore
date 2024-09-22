package us.smartmc.gamescore.instance.game;

import org.bukkit.entity.Player;
import us.smartmc.gamescore.event.GameStatusChangeEvent;
import us.smartmc.gamescore.util.BukkitUtil;

import java.util.HashSet;
import java.util.Set;

public abstract class Game implements IGame {

    protected GameStatus status = GameStatus.WAITING;

    protected final Set<Player> players = new HashSet<>();

    public Game() {

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
