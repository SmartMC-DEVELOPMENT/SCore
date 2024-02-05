import me.imsergioh.pluginsapi.connection.MongoDBConnection;
import org.bson.Document;

public class MongoNames {

    public static void main(String[] args) {
        MongoDBConnection.mainConnection = new MongoDBConnection("smart.dedi1", 27017);

        for (Document document : MongoDBConnection.mainConnection.getDatabase("player_data").getCollection("snowgames_ffa")
                .find()) {
            String id = document.getString("_id");
            Document queryDocument = new Document("_id", id);
            Document data = MongoDBConnection.mainConnection.getDatabase("player_data").getCollection("offline_player_data")
                    .find(new Document("_id", id)).first();
            if (data == null) continue;
            String name = data.getString("name");
            document.put("name", name);
            MongoDBConnection.mainConnection.getDatabase("player_data").getCollection("snowgames_ffa")
                    .deleteMany(queryDocument);
            MongoDBConnection.mainConnection.getDatabase("player_data").getCollection("snowgames_ffa").insertOne(document);
        }
    }

}
