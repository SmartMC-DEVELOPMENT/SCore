package us.smartmc.lobbycosmetics.instance.player;

import java.util.UUID;

public interface ICosmeticPlayerSession {

    void load();
    void unload();

    ICosmeticPlayerData getData();

    UUID getId();
}
