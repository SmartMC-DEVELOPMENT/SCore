package us.smartmc.game.luckytowers.manager;

import me.imsergioh.pluginsapi.instance.manager.ManagerRegistry;
import us.smartmc.game.luckytowers.LuckyTowers;
import us.smartmc.game.luckytowers.instance.game.GameMap;
import us.smartmc.game.luckytowers.instance.game.GameSession;
import us.smartmc.game.luckytowers.instance.game.GameSessionStatus;

import java.util.UUID;

public class GameSessionsManager extends ManagerRegistry<UUID, GameSession> {

    @Override
    public void load() {

    }

    @Override
    public void unload() {

    }

    public int getPlayingCount(GameMap map) {
        int count = 0;
        for (GameSession session : values()) {
            if (session.getMap().equals(map)) count += session.getAlivePlayers().size();
        }
        return count;
    }

    public GameSession createOrGetByName(String mapName, int amount) {
        GameMapManager manager = LuckyTowers.getManager(GameMapManager.class);
        GameMap map = manager.get(mapName);
        if (map == null) return null;
        if (map.isMaintenance()) return null;

        GameSession session = null;
        for (GameSession s : values()) {
            // Check map name
            if (!s.getMap().equals(map)) continue;
            // Check free slots for teams
            int freeSlots = s.getTeams().getFreeSlots();
            if (freeSlots <= 0) continue;
            if (!s.canPlayersJoin(amount)) continue;
            session = s;
            break;
        }

        // If not sessions available create one
        if (session == null) {
            UUID id = UUID.randomUUID();
            session = new GameSession(id, map);
            register(id, session);
        }
        return session;
    }

}
