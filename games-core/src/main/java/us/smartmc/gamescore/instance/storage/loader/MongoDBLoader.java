package us.smartmc.gamescore.instance.storage.loader;

import com.mongodb.client.MongoCollection;
import me.imsergioh.pluginsapi.connection.MongoDBConnection;
import org.bson.Document;

import java.util.Map;

public class MongoDBLoader implements IDataLoader<Document> {

    private final String database, collection;

    public MongoDBLoader(String database, String collection) {
        this.database = database;
        this.collection = collection;
    }

    @Override
    public Map<String, Object> load(Document queryDocument) {
        return getCollection().find(queryDocument).first();
    }

    private MongoCollection<Document> getCollection() {
        return MongoDBConnection.mainConnection.getDatabase(database).getCollection(collection);
    }

}
