package us.smartmc.gamescore.instance.game.map;

import lombok.Getter;
import org.bson.Document;
import us.smartmc.gamescore.instance.game.map.spawn.MapSpawnsData;
import us.smartmc.gamescore.instance.storage.SBackendData;
import us.smartmc.gamescore.manager.map.MapsManager;

import java.util.ArrayList;
import java.util.List;

@Getter
public class GameMapData extends SBackendData {

    public static final String ENABLED = "enabled";
    public static final String MAX_PLAYERS = "max_players";
    public static final String MIN_PLAYERS = "min_players";

    public static final String TEAMS_LIMIT = "teams_limit";
    public static final String TEAMS_NAMES = "teams_names";

    private final List<String> teamsNames = new ArrayList<>();

    private MapSpawnsData spawnsData;

    public GameMapData(GameMap map, MapsManager manager) {
        super(manager.getDatabaseId() + "/" + manager.getCollectionId() + "/" + map.getName());
        load();

        registerDefault(ENABLED, false);
        registerDefault(MAX_PLAYERS, 8);
        registerDefault(MIN_PLAYERS, 2);

        registerDefault(TEAMS_LIMIT, 2);
        registerDefault(TEAMS_NAMES, List.of("blue", "red"));

        save();
    }

    @Override
    public void save() {
        put(TEAMS_NAMES, teamsNames);
        put("spawns", spawnsData);
        super.save();
    }

    @Override
    public void load() {
        super.load();
        if (containsKey(TEAMS_NAMES)) {
            teamsNames.addAll(getList(TEAMS_NAMES, String.class));
        }
        spawnsData = new MapSpawnsData(this);
    }

    public boolean isEnabled() {
        return getBoolean(ENABLED);
    }

    public int getTeamsLimit() {
        return getInteger(TEAMS_LIMIT);
    }

    public int getMinPlayers() {
        return getInteger(MIN_PLAYERS);
    }

    public int getMaxPlayers() {
        return getInteger(MAX_PLAYERS);
    }

    private static Document getQuery(GameMap map) {
        return new Document("_id", map.getName());
    }
}
