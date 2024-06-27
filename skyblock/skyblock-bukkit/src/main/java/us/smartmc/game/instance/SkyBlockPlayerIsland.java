package us.smartmc.game.instance;

import us.smartmc.skyblock.instance.island.ISkyBlockIsland;
import us.smartmc.skyblock.instance.island.IslandGeneratorType;

import java.util.UUID;

public class SkyBlockPlayerIsland implements ISkyBlockIsland {

    private final UUID ownerId;

    public SkyBlockPlayerIsland(UUID ownerId) {
        this.ownerId = ownerId;
    }

    // Loads from manager
    @Override
    public void register() {

    }

    // Unloads from manager
    @Override
    public void unregister() {

    }

    @Override
    public IslandGeneratorType getIslandGeneratorType() {
        return null;
    }

    @Override
    public String displayName() {
        return null;
    }

    @Override
    public UUID getOwnerId() {
        return null;
    }

    @Override
    public UUID getId() {
        return null;
    }
}
