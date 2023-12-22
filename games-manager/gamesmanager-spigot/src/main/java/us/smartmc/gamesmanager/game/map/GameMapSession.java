package us.smartmc.gamesmanager.game.map;

import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import us.smartmc.gamesmanager.game.teams.PlayerTeam;
import us.smartmc.gamesmanager.game.teams.TeamSelector;
import us.smartmc.gamesmanager.util.WorldUtils;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class GameMapSession {

    @Getter
    private final GameMap map;

    private final World tempWorld;

    @Getter
    private final TeamSelector selector;

    public GameMapSession(GameMap map) {
        this.map = map;
        tempWorld = WorldUtils.importWorldFromDir(
                map.getMapDir().getAbsolutePath(),
                "mapSession-" + UUID.randomUUID());
        WorldUtils.registerTempWorld(tempWorld);
        selector = new TeamSelector();
    }

    public void assignTeamToPlayer(Player player) {

    }

    public World getWorld() {
        return tempWorld;
    }
}
