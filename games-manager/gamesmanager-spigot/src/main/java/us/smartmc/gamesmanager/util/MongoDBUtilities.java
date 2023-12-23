package us.smartmc.gamesmanager.util;

import com.mongodb.client.MongoCollection;
import me.imsergioh.pluginsapi.connection.MongoDBConnection;
import org.bson.Document;

public class MongoDBUtilities {

    public static MongoCollection<Document> collection(String database, String collection) {
        return MongoDBConnection.mainConnection.getDatabase(database).getCollection(collection);
    }
}
