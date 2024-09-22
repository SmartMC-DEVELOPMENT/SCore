package us.smartmc.gamescore.instance.storage.loader;

import com.mongodb.client.MongoCollection;
import org.bson.Document;

import java.util.Map;

public class MongoDBLoader<T extends Document> implements IDataLoader<T> {

    private final MongoCollection<Document> collection;

    public MongoDBLoader(MongoCollection<Document> collection) {
        this.collection = collection;
    }

    @Override
    public Map<String, Object> load(Document queryDocument) {
        return collection.find(queryDocument).first();
    }
}
