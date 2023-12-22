package us.smartmc.core.pluginsapi.data.player;

import us.smartmc.core.pluginsapi.connection.MongoDBConnection;
import us.smartmc.core.pluginsapi.data.managment.FactoryValue;
import org.bson.Document;

import java.util.UUID;

public abstract class OfflineCorePlayer<T> extends FactoryValue<T> {

    protected final UUID uuid;

    protected Document offlineDataDocument;
    protected final Document query;

    public OfflineCorePlayer(UUID uuid) {
        this.uuid = uuid;
        this.query = getQuery(uuid);
    }

    @Override
    public void load() {
        offlineDataDocument = getDocument();
    }

    @Override
    public void unload() {
    }

    private Document getDocument() {
        Document documentFound = MongoDBConnection.mainConnection
                .getDatabase("player_data")
                .getCollection("offline_player_data")
                .find(getQuery(uuid)).first();
        return documentFound == null ? query : documentFound;
    }

    private static Document getQuery(UUID uuid) {
        return new Document("_id", uuid.toString());
    }
}
