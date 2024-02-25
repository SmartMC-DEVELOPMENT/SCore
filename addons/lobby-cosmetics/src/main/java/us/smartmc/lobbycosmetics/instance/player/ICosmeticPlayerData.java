package us.smartmc.lobbycosmetics.instance.player;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import me.imsergioh.pluginsapi.connection.MongoDBConnection;
import org.bson.Document;
import us.smartmc.lobbycosmetics.instance.DatabaseTarget;
import us.smartmc.lobbycosmetics.instance.cosmetic.CosmeticType;
import us.smartmc.lobbycosmetics.instance.cosmetic.ICosmetic;

import java.util.*;

public interface ICosmeticPlayerData {

    Document getDocument();

    UUID getId();

    int getUnlocked(CosmeticType type);

    default void save() {
        getCollection().deleteMany(getQuery());
        getCollection().insertOne(getDocument());
    }

    default boolean hasCosmetic(ICosmetic cosmetic) {
        return hasCosmetic(cosmetic.getType(), cosmetic.getId());
    }

    default boolean hasCosmetic(CosmeticType type, String id) {
        return getCosmetics(type).contains(id);
    }

    default Collection<String> getCosmetics(CosmeticType type) {
        return getDocument().getList(type.name(), String.class, new ArrayList<>());
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
