package us.smartmc.moderation.staffmodebungee.instance;

import com.mongodb.client.MongoCollection;
import lombok.Getter;
import me.imsergioh.pluginsapi.connection.MongoDBConnection;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.bson.Document;

public class StaffPlayer {

    @Getter
    private final ProxiedPlayer player;
    private Document document;

    public StaffPlayer(ProxiedPlayer player) {
        this.player = player;
    }

    public void load() {
        document = getCollection().find(getQueryDocument()).first();
        if (document == null) document = getQueryDocument();
    }

    public void unload() {
        getCollection().deleteMany(getQueryDocument());
        getCollection().insertOne(document);
    }

    private Document getQueryDocument() {
        return new Document("_id", player.getUniqueId());
    }

    private static MongoCollection<Document> getCollection() {
        return MongoDBConnection.mainConnection.getDatabase("player_data").getCollection("staff_player");
    }

}
