package us.smartmc.gamescore.instance.event;

import lombok.Getter;
import org.bukkit.entity.Player;
import us.smartmc.gamescore.api.GamesCoreAPI;
import us.smartmc.gamescore.instance.player.GameCorePlayer;
import us.smartmc.gamescore.manager.PlayersManager;

@Getter
public abstract class GameCorePlayerEvent extends GameCoreEvent {

    private final GameCorePlayer corePlayer;
    private final Player bukkitPlayer;

    public GameCorePlayerEvent(GameCorePlayer corePlayer) {
        this.bukkitPlayer = corePlayer.getBukkitPlayer();
        this.corePlayer = corePlayer;
    }

    public GameCorePlayerEvent(Player player) {
        this.bukkitPlayer = player;
        PlayersManager playersManager = GamesCoreAPI.getPlayersManager();
        this.corePlayer = playersManager == null ? null : playersManager.get(player.getUniqueId());
    }

}
