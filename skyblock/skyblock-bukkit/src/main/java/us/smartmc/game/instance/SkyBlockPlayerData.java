package us.smartmc.game.instance;

import com.mongodb.client.MongoCollection;
import lombok.Getter;
import me.imsergioh.pluginsapi.connection.MongoDBConnection;
import org.bson.Document;
import us.smartmc.skyblock.manager.PlayersManager;

import java.util.UUID;


public class SkyBlockPlayerData {

    private static final String DATABASE_NAME = "skyblock";
    private static final String COLLECTION_NAME = "player_data";

    @Getter
    private final UUID id;
    private SkyBlockPlayer skyBlockPlayer;

    @Getter
    private final Document document;

    public SkyBlockPlayerData(UUID id) {
        this.id = id;
        this.document = loadData();
    }

    public SkyBlockPlayerData(SkyBlockPlayer skyBlockPlayer) {
        this(skyBlockPlayer.getId());
    }

    private Document loadData() {
        Document mongoDocument = getCollection().find(getQuery()).first();
        return mongoDocument == null ? getQuery() : mongoDocument;
    }

    public Document getQuery() {
        return new Document("_id", id.toString());
    }

    public SkyBlockPlayer getSkyBlockPlayer() {
        if (skyBlockPlayer == null) {
            skyBlockPlayer = (SkyBlockPlayer) PlayersManager.get(id);
        }
        return skyBlockPlayer;
    }

    public static MongoCollection<Document> getCollection() {
        return MongoDBConnection.mainConnection.getDatabase(DATABASE_NAME).getCollection(COLLECTION_NAME);
    }
}


