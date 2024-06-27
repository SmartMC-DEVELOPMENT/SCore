package us.smartmc.skyblock.instance.island;

import java.util.UUID;

public interface ISkyBlockIsland {

    void register();
    void unregister();

    String displayName();

    UUID getOwnerId();
    UUID getId();

}
