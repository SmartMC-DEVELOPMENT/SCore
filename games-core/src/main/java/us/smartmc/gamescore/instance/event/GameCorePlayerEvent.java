package us.smartmc.gamescore.instance.event;

import org.bukkit.entity.Player;
import us.smartmc.gamescore.api.GamesCoreAPI;
import us.smartmc.gamescore.instance.manager.MapManager;
import us.smartmc.gamescore.instance.player.GameCorePlayer;
import us.smartmc.gamescore.manager.PlayersManager;

public abstract class GameCorePlayerEvent extends GameCoreEvent {

    private final GameCorePlayer corePlayer;
    private final Player bukkitPlayer;

    public GameCorePlayerEvent(Player player, GameCorePlayer corePlayer) {
        this.bukkitPlayer = player;
        this.corePlayer = corePlayer;
    }

    public GameCorePlayerEvent(Player player) {
        this.bukkitPlayer = player;
        PlayersManager playersManager = GamesCoreAPI.getPlayersManager();
        this.corePlayer = playersManager == null ? null : playersManager.get(player.getUniqueId());
    }

    public GameCorePlayer getCorePlayer() {
        return corePlayer;
    }

    public Player getBukkitPlayer() {
        return bukkitPlayer;
    }
}
