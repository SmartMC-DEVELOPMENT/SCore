package us.smartmc.lobbycosmetics.instance;

import me.imsergioh.pluginsapi.instance.player.CorePlayer;
import org.bson.Document;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@DatabaseTarget(database = "player_data", collection = "lobby-cosmetics")
public class CosmeticPlayerSession implements ICosmeticPlayerSession {

    private static final HashMap<UUID, CosmeticPlayerSession> sessions = new HashMap<>();

    private final UUID uuid;
    private Document document;

    public CosmeticPlayerSession(CorePlayer corePlayer) {
        sessions.put(corePlayer.getUUID(), this);
        this.uuid = corePlayer.getUUID();
        this.document = getCosmeticDataDocument();
        load();
    }

    @Override
    public void load() {

    }

    @Override
    public void unload() {

    }

    @Override
    public Set<String> getActiveCosmeticNames() {
        return new HashSet<>(getCosmeticDataDocument().getList("active", String.class));
    }

    @Override
    public Set<String> getCosmeticNames() {
        return new HashSet<>(getCosmeticDataDocument().getList("owned", String.class));
    }

    @Override
    public Document getCosmeticDataDocument() {
        if (document == null) {
            Document query = getQuery();
            Document dbDocument = getCollection().find(query).first();
            if (dbDocument == null) dbDocument = query;
            document = dbDocument;
        }
        return document;
    }

    @Override
    public UUID getUUID() {
        return uuid;
    }

    public static void remove(UUID uuid) {
        CosmeticPlayerSession session = sessions.get(uuid);
        if (session == null) return;
        session.unload();
        sessions.remove(uuid);
    }

    public static CosmeticPlayerSession getSession(CorePlayer corePlayer) {
        return sessions.containsKey(corePlayer.getUUID()) ? sessions.get(corePlayer.getUUID()) : new CosmeticPlayerSession(corePlayer);
    }
}
