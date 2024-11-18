package us.smartmc.survival.instance;

import me.imsergioh.pluginsapi.data.player.OfflineCorePlayer;

import java.util.UUID;

public abstract class SurvivalPlayer extends OfflineCorePlayer<UUID> {

    public SurvivalPlayer(UUID uuid) {
        super(uuid);
    }

    @Override
    public UUID get() {
        return null;
    }
}
