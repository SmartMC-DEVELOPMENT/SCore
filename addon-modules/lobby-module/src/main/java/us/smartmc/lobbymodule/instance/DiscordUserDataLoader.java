package us.smartmc.lobbymodule.instance;

import com.mongodb.client.MongoCollection;
import lombok.Getter;
import me.imsergioh.pluginsapi.connection.MongoDBConnection;
import org.bson.Document;

import java.util.UUID;

public class DiscordUserDataLoader {

    private final UUID id;
    @Getter
    private final Document document;

    public DiscordUserDataLoader(UUID uuid) {
        this.id = uuid;
        this.document = loadDocument();
    }

    public String getName() {
        return document.getString("name");
    }

    private Document loadDocument() {
        MongoCollection<Document> collection = MongoDBConnection.mainConnection.getDatabase("smartbot").getCollection("user_data");
        Document dbDoc = collection.find(getQuery()).first();
        if (dbDoc == null) dbDoc = getQuery();
        return dbDoc;
    }

    private Document getQuery()  {
        return new Document("minecraft-id", id.toString());
    }

}
