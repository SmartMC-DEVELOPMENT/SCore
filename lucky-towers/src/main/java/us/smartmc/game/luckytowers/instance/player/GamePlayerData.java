package us.smartmc.game.luckytowers.instance.player;

import com.mongodb.client.MongoCollection;
import lombok.Getter;
import me.imsergioh.pluginsapi.connection.MongoDBConnection;
import org.bson.Document;

import java.util.UUID;

@Getter
public class GamePlayerData {

    private static final String DATABASE = "player_data";
    private static final String COLLECTION = "luckytowers";

    private final UUID id;

    private final Document document;

    protected GamePlayerData(UUID uuid) {
        this.id = uuid;
        this.document = loadData();
    }

    public void increaseNumber(String key) {
        addToInt(key, 1);
    }

    public void addToInt(String key, int amount) {
        int count = document.getInteger(key, 0);
        document.put(key, count + amount);
    }

    public void save() {
        getCollection().deleteMany(getQuery());
        getCollection().insertOne(document);
    }

    private Document loadData() {
        Document document = getCollection().find(getQuery()).first();
        if (document == null) document = getQuery();
        return document;
    }

    private Document getQuery() {
        return new Document("_id", id.toString());
    }

    private static MongoCollection<Document> getCollection() {
        return MongoDBConnection.mainConnection
                .getDatabase(DATABASE)
                .getCollection(COLLECTION);
    }

}
