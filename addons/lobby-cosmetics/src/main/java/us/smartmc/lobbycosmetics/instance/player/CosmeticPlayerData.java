package us.smartmc.lobbycosmetics.instance.player;

import org.bson.Document;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class CosmeticPlayerData implements ICosmeticPlayerData {

    private final UUID uuid;
    private Document document;

    public CosmeticPlayerData(UUID uuid) {
        this.uuid = uuid;
    }

    @Override
    public CosmeticPlayerSession getSession() {
        return null;
    }

    @Override
    public Document getDocument() {
        Document dbDocument;
        if (document == null) {
            Document query = getQuery();
            dbDocument = getCollection().find(query).first();
            if (dbDocument == null) dbDocument = getQuery();
            return dbDocument;
        }
        return document;
    }

    @Override
    public UUID getId() {
        return uuid;
    }

    @Override
    public Set<String> getActiveCosmeticEntries() {
        return new HashSet<>(getDocument().getList("active", String.class));
    }
}
