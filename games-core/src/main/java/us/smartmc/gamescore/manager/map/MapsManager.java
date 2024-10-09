package us.smartmc.gamescore.manager.map;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import lombok.Getter;
import me.imsergioh.pluginsapi.connection.MongoDBConnection;
import org.bson.Document;
import us.smartmc.gamescore.api.GamesCoreAPI;
import us.smartmc.gamescore.instance.game.map.GameMap;
import us.smartmc.gamescore.instance.manager.MapManager;

@Getter
public class MapsManager extends MapManager<String, GameMap> {

    @Getter
    private final MapsGridManager gridManager;
    private final String databaseId, collectionId;

    public MapsManager() {
        this.databaseId = "game_maps";
        this.collectionId = GamesCoreAPI.getApi().getGameId();
        this.gridManager = new MapsGridManager(this);

        if (MongoDBConnection.mainConnection == null) {

        }

        // Load all documents into collection (GameMap references)
        MongoCollection<Document> collection = MongoDBConnection.mainConnection.getDatabase(databaseId).getCollection(collectionId);
        try (MongoCursor<Document> cursor = collection.find().iterator()) {
            while (cursor.hasNext()) {
                Document doc = cursor.next();
                register(doc.getString("_id"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public GameMap createValueByKey(String name) {
        return new GameMap(name, this);
    }
}
