package us.smartmc.gamescore.manager;

import us.smartmc.gamescore.instance.manager.MapManager;
import us.smartmc.gamescore.instance.player.PlayerTeam;

import java.util.UUID;

public class PlayerTeamsManager extends MapManager<UUID, PlayerTeam> {

    @Override
    public PlayerTeam createValueByKey(UUID key) {
        return null;
    }
}
