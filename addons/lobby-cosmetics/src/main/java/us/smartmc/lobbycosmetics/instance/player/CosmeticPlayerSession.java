package us.smartmc.lobbycosmetics.instance.player;

import me.imsergioh.pluginsapi.instance.player.CorePlayer;
import us.smartmc.lobbycosmetics.instance.DatabaseTarget;

import java.util.UUID;

public class CosmeticPlayerSession implements ICosmeticPlayerSession {

    private final UUID uuid;
    private CosmeticPlayerData cosmeticsData;

    public CosmeticPlayerSession(CorePlayer corePlayer) {
        this.uuid = corePlayer.getUUID();
        load();
    }

    @Override
    public void load() {
        cosmeticsData = new CosmeticPlayerData(uuid);
    }

    @Override
    public void unload() {
        if (cosmeticsData != null) cosmeticsData.save();
    }

    @Override
    public ICosmeticPlayerData getData() {
        return cosmeticsData;
    }

    @Override
    public UUID getId() {
        return uuid;
    }
}
