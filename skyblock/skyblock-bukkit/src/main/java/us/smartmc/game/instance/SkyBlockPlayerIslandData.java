package us.smartmc.game.instance;

import com.mongodb.client.MongoCollection;
import lombok.Getter;
import me.imsergioh.pluginsapi.connection.MongoDBConnection;
import me.imsergioh.pluginsapi.util.SyncUtil;
import org.bson.Document;
import org.bukkit.Bukkit;
import us.smartmc.game.event.SkyBlockIslandPlayerDataLoadedEvent;

import java.util.UUID;

@Getter
public class SkyBlockPlayerIslandData {

    private static final String DATABASE_NAME = "skyblock";
    private static final String COLLECTION_NAME = "island_data";

    private final UUID islandId;
    private final UUID ownerId;

    private final Document document;

    public SkyBlockPlayerIslandData(SkyBlockPlayerIsland island) {
        this.islandId = UUID.randomUUID();
        this.ownerId = island.getOwnerId();
        document = loadData();
        SyncUtil.async(() -> {
            Bukkit.getPluginManager().callEvent(new SkyBlockIslandPlayerDataLoadedEvent(island.getSkyBlockPlayer(), this));
        });
    }

    public void saveData() {
        getCollection().deleteMany(getQuery());
        getCollection().insertOne(document);
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
