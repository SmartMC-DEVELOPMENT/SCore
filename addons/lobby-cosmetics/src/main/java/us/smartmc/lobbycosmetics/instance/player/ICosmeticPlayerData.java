package us.smartmc.lobbycosmetics.instance.player;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import me.imsergioh.pluginsapi.connection.MongoDBConnection;
import org.bson.Document;
import us.smartmc.lobbycosmetics.instance.DatabaseTarget;
import us.smartmc.lobbycosmetics.instance.cosmetic.CosmeticType;
import us.smartmc.lobbycosmetics.instance.cosmetic.ICosmetic;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public interface ICosmeticPlayerData {

    CosmeticPlayerSession getSession();
    Document getDocument();

    UUID getId();

    default Set<ICosmetic> getActiveCosmetics() {
        Set<ICosmetic> list = new HashSet<>();
        for (String entry : getActiveCosmeticEntries()) {
            String[] args = entry.split("@");
            CosmeticType type = CosmeticType.valueOf(args[0]);
            String id = args[1];

        }
        return list;
    }
    Set<String> getActiveCosmeticEntries();

    default void save() {
        getCollection().deleteMany(getQuery());
        getCollection().insertOne(getDocument());
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

    default Document getQuery() {
        return new Document("_id", getId().toString());
    }
}
