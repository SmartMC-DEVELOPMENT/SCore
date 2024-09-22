package us.smartmc.gamescore.instance.storage.saver;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.ReplaceOptions;
import me.imsergioh.pluginsapi.connection.MongoDBConnection;
import org.bson.Document;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class MongoDBSaver implements IDataSaver {

    private final String database, collection;
    private final Document query;

    public MongoDBSaver(String database, String collection, Document query) {
        this.database = database;
        this.collection = collection;
        this.query = query;
    }

    @Override
    public void save(boolean async, Map<String, Object> data) {
        Runnable action = () -> {
            getCollection().replaceOne(query, new Document(data), new ReplaceOptions().upsert(true));
        };

        if (async) {
            CompletableFuture.runAsync(action).exceptionally(ex -> {
                // Manejo de excepciones
                ex.printStackTrace();
                return null;
            });
            return;
        }
        action.run();
    }

    private MongoCollection<Document> getCollection() {
        return MongoDBConnection.mainConnection.getDatabase(database).getCollection(collection);
    }

}
