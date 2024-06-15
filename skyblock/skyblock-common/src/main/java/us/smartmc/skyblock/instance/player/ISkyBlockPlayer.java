package us.smartmc.skyblock.instance.player;

import java.util.UUID;

public interface ISkyBlockPlayer {

    void load();

    void register();
    void unregister();

    UUID getId();

}
