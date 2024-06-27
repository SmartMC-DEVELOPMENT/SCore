package us.smartmc.skyblock.manager;

import us.smartmc.skyblock.instance.island.ISkyBlockIsland;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class IslandsManager {

    private static final Map<UUID, ISkyBlockIsland> islands = new HashMap<>();

    public static void register(ISkyBlockIsland island) {
        if (island == null) return;
        islands.put(island.getId(), island);
        island.register();
    }

    public static void unregister(UUID id) {
        ISkyBlockIsland island = islands.get(id);
        if (island == null) return;
        unregister(island);
        island.unregister();
    }

    public static void unregister(ISkyBlockIsland island) {
        UUID id = island.getId();
        if (!islands.containsKey(id)) return;
        islands.remove(id).unregister();
    }

    public static ISkyBlockIsland get(UUID id) {
        return islands.get(id);
    }

}
