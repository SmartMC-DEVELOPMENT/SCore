package us.smartmc.gamescore.instance.storage;

import com.mongodb.client.model.IndexOptions;
import lombok.Getter;
import me.imsergioh.pluginsapi.connection.MongoDBConnection;
import org.bson.Document;
import us.smartmc.gamescore.instance.storage.loader.MongoDBLoader;
import us.smartmc.gamescore.instance.storage.saver.MongoDBSaver;

public class MongoDBData extends Data<MongoDBLoader<Document>, MongoDBSaver, Document> {

    private final String database, collection;

    @Getter
    private final Document query;

    public MongoDBData(String database, String collection, Document query) {
        this.collection = collection;
        this.database = database;
        this.query = query;
    }

    public void createIndex(String name) {
        MongoDBConnection.mainConnection.getDatabase(database).getCollection(collection)
                .createIndex(new Document(name, 1), new IndexOptions());
    }

    @Override
    public String getName() {
        return "MongoDB@" + database + "/" + collection + "/" + query;
    }

    @Override
    public MongoDBLoader<Document> getLoaderInstance() {
        return new MongoDBLoader<>(MongoDBConnection.mainConnection.getDatabase(database).getCollection(collection));
    }

    @Override
    public MongoDBSaver getSaverInstance() {
        return new MongoDBSaver(MongoDBConnection.mainConnection.getDatabase(database).getCollection(collection), query);
    }

    @Override
    public Document getDataLoaderObject() {
        return query;
    }
}
