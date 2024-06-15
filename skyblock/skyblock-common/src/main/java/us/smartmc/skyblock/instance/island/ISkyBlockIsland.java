package us.smartmc.skyblock.instance.island;

import java.util.UUID;

public interface ISkyBlockIsland {

    void register();
    void unregister();

    void load();

    IslandGeneratorType getIslandGeneratorType();
    String displayName();

    UUID getOwnerId();
    UUID getId();

}
