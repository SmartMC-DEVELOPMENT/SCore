package us.smartmc.gamescore.instance.game.map;

import org.bson.Document;
import us.smartmc.gamescore.instance.storage.MongoDBData;
import us.smartmc.gamescore.manager.map.MapsManager;

import java.util.List;

public class GameMapData extends MongoDBData {

    public static final String ENABLED = "enabled";
    public static final String MAX_PLAYERS = "max_players";
    public static final String MIN_PLAYERS = "min_players";

    public static final String TEAMS_LIMIT = "teams_limit";
    public static final String TEAMS_NAMES = "teams_names";

    public GameMapData(GameMap map, MapsManager manager) {
        super(manager.getDatabaseId(), manager.getCollectionId(), getQuery(map));
        load();

        registerDefault(ENABLED, false);
        registerDefault(MAX_PLAYERS, 8);
        registerDefault(MIN_PLAYERS, 2);

        registerDefault(TEAMS_LIMIT, 2);
        registerDefault(TEAMS_NAMES, List.of("blue", "red"));

        save();
    }

    public boolean isEnabled() {
        return getBoolean(ENABLED);
    }

    public List<String> getTeamsNames() {
        return getList(TEAMS_NAMES, String.class);
    }

    public int getTeamsLimit() {
        return getInt(TEAMS_LIMIT);
    }

    public int getMinPlayers() {
        return getInt(MIN_PLAYERS);
    }

    public int getMaxPlayers() {
        return getInt(MAX_PLAYERS);
    }

    private static Document getQuery(GameMap map) {
        return new Document("_id", map.getName());
    }
}
