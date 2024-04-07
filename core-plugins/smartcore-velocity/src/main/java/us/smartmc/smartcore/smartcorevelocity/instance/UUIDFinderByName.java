package us.smartmc.smartcore.smartcorevelocity.instance;

import lombok.Getter;
import org.bson.Document;
import us.smartmc.smartcore.smartcorevelocity.manager.OfflinePlayerDataManager;

import java.util.UUID;

public class UUIDFinderByName {

    private final String name;
    @Getter
    private final Document document;

    public UUIDFinderByName(String name) throws CorePluginException {
        this.name = name.toLowerCase();
        document = OfflinePlayerDataManager.getCollection().find(getQuery()).first();
        if (document == null) throw new CorePluginException("User not found!");
    }

    public UUID getUUID() {
        return UUID.fromString(document.getString("_id"));
    }

    public boolean wasFound() {
        return document == null;
    }

    private Document getQuery() {
        return new Document("lowercase_name", name.toLowerCase());
    }

}
