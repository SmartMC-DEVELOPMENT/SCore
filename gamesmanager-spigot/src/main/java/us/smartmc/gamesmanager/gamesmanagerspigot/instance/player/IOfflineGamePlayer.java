package us.smartmc.gamesmanager.gamesmanagerspigot.instance.player;

import us.smartmc.gamesmanager.gamesmanagerspigot.instance.player.data.IOfflineGamePlayerData;
import us.smartmc.gamesmanager.gamesmanagerspigot.manager.GamePlayerManager;

import java.util.UUID;

public interface IOfflineGamePlayer {

    void load();
    void unload();

    IOfflineGamePlayerData getData();
    UUID getUUID();

    GamePlayerManager<?> getManager();

}
