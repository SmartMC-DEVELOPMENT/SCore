package us.smartmc.gamesmanager.gamesmanagerspigot.manager;

import us.smartmc.gamesmanager.gamesmanagerspigot.instance.player.IOfflineGamePlayer;

import java.util.HashMap;
import java.util.UUID;

public class GamePlayerManager<T extends IOfflineGamePlayer> {

    private final HashMap<UUID, T> players = new HashMap<>();

    public void register(T player) {
        players.put(player.getUUID(), player);
        player.load();
    }

    public void unregister(UUID uuid) {
        get(uuid).unload();
        players.remove(uuid);
    }

    public T get(UUID uuid) {
        return players.get(uuid);
    }
}
