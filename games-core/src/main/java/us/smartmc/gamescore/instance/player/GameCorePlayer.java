package us.smartmc.gamescore.instance.player;

import lombok.Getter;
import org.bukkit.entity.Player;
import us.smartmc.gamescore.api.GamesCoreAPI;

import java.util.UUID;

public class GameCorePlayer {

    private final UUID uuid;

    @Getter
    private PlayerStatus status = PlayerStatus.LOBBY;

    public GameCorePlayer(UUID uuid) {
        this.uuid = uuid;
    }

    public static GameCorePlayer of(Player player) {
        return of(player.getUniqueId());
    }

    public static GameCorePlayer of(UUID uuid) {
        return GamesCoreAPI.getPlayersManager().getOrCreate(uuid);
    }

}
