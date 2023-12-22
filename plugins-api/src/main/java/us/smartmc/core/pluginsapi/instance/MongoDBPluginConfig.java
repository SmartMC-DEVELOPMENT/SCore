package us.smartmc.core.pluginsapi.instance;

import us.smartmc.core.pluginsapi.connection.MongoDBConnection;
import org.bson.Document;

public class MongoDBPluginConfig extends Document implements IPluginConfig {

    private final String database, collection;
    private final Document queryDocument;

    public MongoDBPluginConfig(String database, String collection, Document query) {
        this.database = database;
        this.collection = collection;
        this.queryDocument = query;
        put("_id", query.get("_id"));
    }

    @Override
    public MongoDBPluginConfig save() {
        MongoDBConnection.mainConnection
                .getDatabase(database)
                .getCollection(collection).deleteMany(queryDocument);
        MongoDBConnection.mainConnection
                .getDatabase(database)
                .getCollection(collection).insertOne(this);
        return this;
    }

    @Override
    public MongoDBPluginConfig load() {
        Document document = MongoDBConnection.mainConnection
                .getDatabase(database)
                .getCollection(collection)
                .find(queryDocument).first();
        if (document != null) putAll(document);
        if (document == null) putAll(queryDocument);
        return this;
    }

    @Override
    public MongoDBPluginConfig registerDefault(String path, Object value) {
        putIfAbsent(path, value);
        return this;
    }

    public String getDatabase() {
        return database;
    }

    public String getCollection() {
        return collection;
    }

    public Document getQueryDocument() {
        return queryDocument;
    }
}
