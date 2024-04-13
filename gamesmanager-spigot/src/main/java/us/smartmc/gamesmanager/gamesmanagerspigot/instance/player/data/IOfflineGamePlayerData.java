package us.smartmc.gamesmanager.gamesmanagerspigot.instance.player.data;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import me.imsergioh.pluginsapi.connection.MongoDBConnection;
import org.bson.Document;

import java.util.UUID;

public interface IOfflineGamePlayerData {

    void load();
    void save();

    void register(String key, Object value);
    void set(String key, Object value);

    void addNumberStat(String key);
    void subtractNumberStat(String key);
    void resetNumberStat(String key);

    Number getNumberStat(String key);
    Object get(String key);

    Document getDocument();
    Document getQueryDocument();
    UUID getUUID();

    MongoDatabase getDatabase();
    MongoCollection<Document> getCollection();
    MongoDBConnection getMongoConnection();

}
