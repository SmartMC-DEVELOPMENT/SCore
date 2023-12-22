package me.imsergioh.smartcorewaterfall.instance;

import com.mongodb.client.MongoCollection;
import us.smartmc.core.pluginsapi.connection.MongoDBConnection;
import org.bson.Document;

public class MongoDataManager {

    protected final MongoCollection<Document> collection;

    public MongoDataManager(String database, String collectionName) {
        collection = MongoDBConnection.mainConnection.getDatabase(database)
                .getCollection(collectionName);
    }

    public void insert(Document document) {
        collection.insertOne(document);
    }

    public boolean existsDocument(Document query) {
        return collection.countDocuments(query) > 0;
    }

    public MongoCollection<Document> getCollection() {
        return collection;
    }
}
