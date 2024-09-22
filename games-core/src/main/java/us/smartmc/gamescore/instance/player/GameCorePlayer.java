package us.smartmc.gamescore.instance.player;

import lombok.Getter;
import org.bukkit.entity.Player;
import us.smartmc.gamescore.api.GamesCoreAPI;
import us.smartmc.gamescore.event.GamePlayerStatusChangeEvent;
import us.smartmc.gamescore.util.BukkitUtil;

import java.util.UUID;

public class GameCorePlayer {

    private final UUID uuid;

    @Getter
    private PlayerStatus status = PlayerStatus.LOBBY;

    @Getter
    private final GamePlayerStats stats;

    public GameCorePlayer(UUID uuid) {
        this.uuid = uuid;
        this.stats = new GamePlayerStats(uuid);

        // Register to the playersManager
        GamesCoreAPI.getPlayersManager().put(uuid, this);

        // TODO: REMOVE THIS CONSUME PLAYER WHEN TESTED IN BUKKIT! :D
        BukkitUtil.consumePlayer(uuid, player -> {
            player.sendMessage("DEBUG: Se ha creado instancia GameCorePlayer!");
        });
    }

    public void setStatus(PlayerStatus status) {
        PlayerStatus previousStatus = this.status;
        this.status = status;
        BukkitUtil.getPlayer(uuid).ifPresent(bukkitPlayer -> {
            BukkitUtil.callEvent(new GamePlayerStatusChangeEvent(bukkitPlayer, previousStatus, status));
        });
    }

    public UUID getUUID() {
        return uuid;
    }

    public static GameCorePlayer of(Player player) {
        return of(player.getUniqueId());
    }

    public static GameCorePlayer of(UUID uuid) {
        return GamesCoreAPI.getPlayersManager().getOrCreate(uuid);
    }

}
