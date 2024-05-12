package us.smartmc.game.luckytowers.manager;

import me.imsergioh.pluginsapi.instance.manager.ManagerRegistry;
import us.smartmc.game.luckytowers.LuckyTowers;
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

    public GameSession createOrGetByName(String mapName, int amount) {
        GameSession session = null;
        for (GameSession s : values()) {
            // Check map name
            if (!s.getMap().getName().equals(mapName)) continue;

            // Check free slots for teams
            int freeSlots = s.getTeams().getFreeSlots();
            System.out.println("Free slots=" + +freeSlots);
            if (freeSlots <= 0) continue;
            if (!s.canPlayersJoin(amount)) continue;
            session = s;
            break;
        }
        if (session == null) {
            UUID id = UUID.randomUUID();
            GameMapManager manager = LuckyTowers.getManager(GameMapManager.class);
            session = new GameSession(id, manager.get(mapName));
            register(id, session);
        }
        return session;
    }

}
