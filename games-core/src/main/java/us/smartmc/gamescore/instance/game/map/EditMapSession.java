package us.smartmc.gamescore.instance.game.map;

import lombok.Getter;
import org.bukkit.entity.Player;
import us.smartmc.gamescore.instance.manager.MapManager;
import us.smartmc.gamescore.manager.map.EditMapSessionsManager;
import us.smartmc.gamescore.manager.map.MapsManager;

import java.util.ArrayList;
import java.util.Set;

@Getter
public class EditMapSession {

    private final Player player;
    private final GameMap map;

    public EditMapSession(Player player, String mapName) {
        this.player = player;
        MapsManager manager = MapsManager.getManager(MapsManager.class);
        if (manager != null) this.map = manager.get(mapName);
        else this.map = null;
    }

    public void setTeamsNames(Set<String> teamNames) {
        setAndSave(GameMapData.TEAMS_LIMIT, new ArrayList<>(teamNames));
    }

    public void setTeamsLimit(int amount) {
        setAndSave(GameMapData.TEAMS_LIMIT, amount);
    }

    public void setMax(int amount) {
        setAndSave(GameMapData.MAX_PLAYERS, amount);
    }

    public void setMin(int amount) {
        setAndSave(GameMapData.MIN_PLAYERS, amount);
    }

    public boolean toggleEnabled() {
        setAndSave(GameMapData.ENABLED, !map.isEnabled());
        return map.isEnabled();
    }

    private void setAndSave(String key, Object value) {
        map.getData().save();
    }

    public static EditMapSession of(Player player) {
        EditMapSessionsManager manager = MapManager.getManager(EditMapSessionsManager.class);
        if (manager == null) return null;
        return manager.of(player);
    }
}
