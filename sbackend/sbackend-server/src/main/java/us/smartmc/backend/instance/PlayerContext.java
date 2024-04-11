package us.smartmc.backend.instance;

import lombok.Getter;

import java.util.UUID;

public class PlayerContext {

    private final UUID uuid;

    @Getter
    private final PlayerCache cache;

    public PlayerContext(UUID uuid) {
        this.uuid = uuid;
        this.cache = new PlayerCache();
        cache.set("testvalue", "Esto es un valor de test predeterminado en PlayerCache a ver que tal");
    }

    public UUID getUniqueId() {
        return uuid;
    }
}
