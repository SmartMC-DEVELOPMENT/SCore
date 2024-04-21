package us.smartmc.backend.instance.player;

import lombok.Getter;

import java.util.UUID;

public class PlayerContext {

    private final UUID playerId;

    @Getter
    private final PlayerCache cache;

    public PlayerContext(UUID uuid) {
        this.playerId = uuid;
        this.cache = new PlayerCache(uuid);
        System.out.println("PlayerContext set!");
    }

    public UUID getUniqueId() {
        return playerId;
    }
}
