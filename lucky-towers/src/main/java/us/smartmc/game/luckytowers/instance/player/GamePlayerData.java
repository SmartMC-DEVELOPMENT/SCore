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

    public long getBigNumber(String key) {
        if (!document.containsKey(key)) return 0;
        return document.get(key, Number.class).longValue();
    }

    public int getInt(String key) {
        Number number = document.get(getStreakKey(key), Number.class);
        if (number == null) return 0;
        return number.intValue();
    }

    public void increaseStreak(String key) {
        long bestStreak = getBestStreak(key);
        increaseNumber(getStreakKey(key));
        long currentStreak = getStreak(key);

        // Check if current streak is more than last best streak and updates it if is more
        if (bestStreak < currentStreak) {
            document.put(getBestStreakKey(key), currentStreak);
        }
    }

    public void resetStreak(String key) {
        resetInt(getStreakKey(key));
    }

    public void increaseNumber(String key) {
        addToNumber(key, 1);
    }

    public long getStreak(String key) {
        Number number = document.get(getStreakKey(key), Number.class);
        if (number == null) return 0;
        return number.longValue();
    }

    public long getBestStreak(String key) {
        Number number = document.get(getStreakKey(key), Number.class);
        if (number == null) return 0;
        return number.longValue();
    }

    public void addToNumber(String key, long amount) {
        Number number = document.get(key, Number.class);
        long count = number != null ? number.longValue() : 0;
        document.put(key, count + amount);
    }

    public void resetInt(String key) {
        document.put(key, (long) 0);
    }

    public static String getBestStreakKey(String key) {
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
