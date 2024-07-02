package us.smartmc.game.instance;

import com.mongodb.client.MongoCollection;
import lombok.Getter;
import me.imsergioh.pluginsapi.connection.MongoDBConnection;
import me.imsergioh.pluginsapi.util.SyncUtil;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import us.smartmc.core.util.ConfigUtils;
import us.smartmc.game.SkyBlockPlugin;
import us.smartmc.game.event.SkyBlockIslandPlayerDataLoadedEvent;

import java.util.UUID;

@Getter
public class SkyBlockPlayerIslandData {

    private static final String DATABASE_NAME = "skyblock";
    private static final String COLLECTION_NAME = "island_data";

    private static final String MIN_LOC_KEY = "min_location";
    private static final String MAX_LOC_KEY = "max_location";
    private static final String SPAWN_LOC_KEY = "spawn_location";

    private final UUID islandId;
    private final UUID ownerId;

    private final Document document;

    public SkyBlockPlayerIslandData(SkyBlockPlayerIsland island) {
        this.islandId = island.getIslandId();
        this.ownerId = island.getOwnerId();
        document = loadData();

        if (island.getSkyBlockPlayer() != null)
            SyncUtil.async(() -> {
                Bukkit.getPluginManager().callEvent(new SkyBlockIslandPlayerDataLoadedEvent(island.getSkyBlockPlayer(), this));
            });
    }

    public boolean hasIslandReference() {
        return getDocument().containsKey(MIN_LOC_KEY) && getDocument().containsKey(MAX_LOC_KEY) && getDocument().containsKey(SPAWN_LOC_KEY);
    }

    public void setupIslandData(Location min, Location max, Location spawn) {
        setMinLocation(min);
        setMaxLocation(max);
        setSpawnLocation(spawn);
        Bukkit.getScheduler().runTaskAsynchronously(SkyBlockPlugin.getPlugin(), this::saveData);
    }

    public void setSpawnLocation(Location location) {
        setLocation(location, SPAWN_LOC_KEY);
    }

    public Location getSpawnLocation(World world) {
        return getLocation(world, SPAWN_LOC_KEY);
    }

    public void setMinLocation(Location location) {
        setLocation(location, MIN_LOC_KEY);
    }

    public void setMaxLocation(Location location) {
        setLocation(location, MAX_LOC_KEY);
    }

    public Location getMinLocation(World world) {
        return getLocation(world, MIN_LOC_KEY);
    }

    public Location getMaxLocation(World world) {
        return getLocation(world, MAX_LOC_KEY);
    }

    private void setLocation(Location location, String key) {
        getDocument().put(key, ConfigUtils.locationToDocument(location, false, false));
    }

    private Location getLocation(World world, String key) {
        return ConfigUtils.documentToLocation(world, getDocument().get(key, Document.class));
    }

    public void saveData() {
        SyncUtil.async(() -> {
            getCollection().deleteMany(getQuery());
            getCollection().insertOne(document);
        });
    }

    public Document loadData() {
        Document mongoDoc = getCollection().find(getQuery()).first();
        if (mongoDoc == null) mongoDoc = getQuery();
        return mongoDoc;
    }

    private Document getQuery() {
        return new Document("_id", islandId.toString());
    }

    private static MongoCollection<Document> getCollection() {
        return MongoDBConnection.mainConnection
                .getDatabase(DATABASE_NAME)
                .getCollection(COLLECTION_NAME);
    }

}
