package us.smartmc.skyblock.manager;

import us.smartmc.skyblock.instance.player.ISkyBlockPlayer;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayersManager {

    private static final Map<UUID, ISkyBlockPlayer> players = new HashMap<>();

    public static void register(ISkyBlockPlayer skyBlockPlayer) {
        if (skyBlockPlayer == null) return;
        players.put(skyBlockPlayer.getId(), skyBlockPlayer);
        skyBlockPlayer.register();
    }

    public static void unregister(UUID id) {
        ISkyBlockPlayer skyBlockPlayer = players.get(id);
        if (skyBlockPlayer == null) return;
        unregister(skyBlockPlayer);
    }

    public static void unregister(ISkyBlockPlayer skyBlockPlayer) {
        UUID id = skyBlockPlayer.getId();
        if (!players.containsKey(id)) return;
        players.remove(id).unregister();
    }

    public static ISkyBlockPlayer get(UUID uuid) {
        return players.get(uuid);
    }

}
