package us.smartmc.skyblock.instance.island;

import java.util.UUID;

public interface ISkyBlockIsland {

    IslandGeneratorType getIslandGeneratorType();
    String displayName();

    UUID getOwnerId();
    UUID getId();

}
