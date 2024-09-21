package us.smartmc.gamescore.manager;

import us.smartmc.gamescore.instance.manager.MapManager;

import java.util.UUID;

public class PlayerTeamsManager extends MapManager<UUID, GameTeamsManager> {

    @Override
    public GameTeamsManager createValueByKey(UUID key) {
        return null;
    }
}
