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

    private static final String COINS_KEY = "coins";
    private static final String PLAYED_TIME_KEY = "played_time";

    @Getter
    private final UUID id;
    private SkyBlockPlayer skyBlockPlayer;

    @Getter
    private final Document document;

    private long startedSession;

    public SkyBlockPlayerData(UUID id) {
        this.id = id;
        this.document = loadData();
    }

    public SkyBlockPlayerData(SkyBlockPlayer skyBlockPlayer) {
        this(skyBlockPlayer.getId());
    }

    public void registerEndPlayTime(long startTimestamp) {
        long end = System.currentTimeMillis() / 1000;
        long difference = end - startTimestamp;
        addLongNumber(PLAYED_TIME_KEY, difference);
    }

    public void addCoins(long amount) {
        addLongNumber(COINS_KEY, amount);
    }

    private void addLongNumber(String key, long amount) {
        long currentAmount = document.get(key, 0L);
        long newAmount = currentAmount + amount;
        document.put(key, newAmount);
    }

    public long getCoins() {
        return document.get(COINS_KEY, 0L);
    }

    public void saveData() {
        getCollection().deleteMany(getQuery());
        getCollection().insertOne(document);
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


