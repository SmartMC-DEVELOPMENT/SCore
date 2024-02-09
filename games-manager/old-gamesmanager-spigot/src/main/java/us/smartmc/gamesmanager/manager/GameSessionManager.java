package us.smartmc.gamesmanager.manager;

import us.smartmc.gamesmanager.game.GameSession;

import java.util.HashMap;
import java.util.UUID;

public class GameSessionManager {

    private static final HashMap<UUID, GameSession> sessions = new HashMap<>();


    public static void register(GameSession instance) {
        if (sessions.containsKey(instance.getID())) return;
        sessions.put(instance.getID(), instance);
    }

    public static void unregister(UUID uuid) {
        if (!sessions.containsKey(uuid)) return;
        get(uuid).unload();
        sessions.remove(uuid);
    }

    public static GameSession get(UUID id) {
        return sessions.get(id);
    }
}
