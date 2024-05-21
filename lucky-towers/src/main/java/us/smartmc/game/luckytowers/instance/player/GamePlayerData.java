package us.smartmc.game.luckytowers.instance.player;

import com.mongodb.client.MongoCollection;
import lombok.Getter;
import me.imsergioh.pluginsapi.connection.MongoDBConnection;
import org.bson.Document;
import org.bukkit.entity.Player;

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

    public void increaseStreak(String key) {
        int bestStreak = getBestStreak(key);
        increaseNumber(getStreakKey(key));
        int currentStreak = getStreak(key);

        // Check if current streak is more than last best streak and updates it if is more
        if (bestStreak < currentStreak) {
            document.put(getBestStreakKey(key), currentStreak);
        }
    }

    public void resetStreak(String key) {
        resetInt(getStreakKey(key));
    }

    public void increaseNumber(String key) {
        addToInt(key, 1);
    }

    public int getStreak(String key) {
        return document.getInteger(getStreakKey(key), 0);
    }

    public int getBestStreak(String key) {
        return document.getInteger(getBestStreakKey(key), 0);
    }

    public void addToInt(String key, int amount) {
        int count = document.getInteger(key, 0);
        document.put(key, count + amount);
    }

    public void resetInt(String key) {
        document.put(key, 0);
    }

    private static String getBestStreakKey(String key) {
        return "bestStreak-" + key;
    }

    private static String getStreakKey(String key) {
        return "streak-" + key;
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

    public static GamePlayerData of(Player player) {
        return GamePlayer.get(player.getUniqueId()).getData();
    }

}
