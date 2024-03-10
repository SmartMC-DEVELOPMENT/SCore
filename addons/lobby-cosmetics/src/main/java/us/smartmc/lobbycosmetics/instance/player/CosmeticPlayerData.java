package us.smartmc.lobbycosmetics.instance.player;

import org.bson.Document;
import us.smartmc.lobbycosmetics.instance.DatabaseTarget;
import us.smartmc.lobbycosmetics.instance.cosmetic.CosmeticType;

import java.util.*;

@DatabaseTarget(database = "player_data", collection = "lobby_cosmetics")
public class CosmeticPlayerData implements ICosmeticPlayerData {

    private final UUID uuid;
    private Document document;

    public CosmeticPlayerData(UUID uuid) {
        this.uuid = uuid;
    }

    @Override
    public Document getDocument() {
        if (document == null) {
            Document query = getQuery();
            Document dbDocument = getCollection().find(query).first();
            if (dbDocument == null) dbDocument = query;
            document = dbDocument;
        }
        return document;
    }

    @Override
    public UUID getId() {
        return uuid;
    }

    @Override
    public int getUnlocked(CosmeticType type) {
        return getDocument().getList(type.name(), String.class, new ArrayList<>()).size();
    }
}
