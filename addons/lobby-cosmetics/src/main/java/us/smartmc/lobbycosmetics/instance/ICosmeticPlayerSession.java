package us.smartmc.lobbycosmetics.instance;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import me.imsergioh.pluginsapi.connection.MongoDBConnection;
import org.bson.Document;

import java.util.Set;
import java.util.UUID;

public interface ICosmeticPlayerSession {

    void load();
    void unload();

    Set<String> getActiveCosmeticNames();
    Set<String> getCosmeticNames();
    Document getCosmeticDataDocument();

    UUID getUUID();

    default Document getQuery() {
        return new Document("_id", getUUID().toString());
    }

    default MongoDatabase getDatabase() {
        return MongoDBConnection.mainConnection.getDatabase(getDatabaseTarget().database());
    }

    default MongoCollection<Document> getCollection() {
        return getDatabase().getCollection(getDatabaseTarget().collection());
    }

    default DatabaseTarget getDatabaseTarget() {
        return getClass().getDeclaredAnnotation(DatabaseTarget.class);
    }
}
