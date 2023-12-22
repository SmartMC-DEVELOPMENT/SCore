package us.smartmc.gamesmanager.util;

import com.mongodb.client.MongoCollection;
import org.bson.Document;
import us.smartmc.core.pluginsapi.connection.MongoDBConnection;

public class MongoDBUtilities {

    public static MongoCollection<Document> collection(String database, String collection) {
        return MongoDBConnection.mainConnection.getDatabase(database).getCollection(collection);
    }
}
