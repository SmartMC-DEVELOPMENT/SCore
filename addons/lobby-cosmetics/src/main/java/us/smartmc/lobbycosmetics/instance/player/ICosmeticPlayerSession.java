package us.smartmc.lobbycosmetics.instance.player;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import me.imsergioh.pluginsapi.connection.MongoDBConnection;
import org.bson.Document;
import us.smartmc.lobbycosmetics.instance.DatabaseTarget;

import java.util.UUID;

public interface ICosmeticPlayerSession {

    void load();
    void unload();

    ICosmeticPlayerData getData();

    UUID getId();
}
