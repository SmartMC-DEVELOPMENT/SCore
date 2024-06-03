package us.smartmc.smartbot.util;

import com.mongodb.client.MongoCollection;
import org.bson.Document;
import us.smartmc.smartbot.connection.MongoDBConnection;

public class MinecraftLinkUtil {

    public static MongoCollection<Document> getUserDataCollection() {
        return MongoDBConnection.mainConnection.getDatabase("smartbot").getCollection("user_data");
    }

    public static MongoCollection<Document> getCorePlayerDataCollection() {
        return MongoDBConnection.mainConnection.getDatabase("player_data").getCollection("core_players");
    }

    public static Document getDiscordUserDataQuery(long userId) {
        return new Document("_id", String.valueOf(userId));
    }
}
