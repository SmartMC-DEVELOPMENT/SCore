package me.imsergioh.smartcorewaterfall.instance;

import me.imsergioh.smartcorewaterfall.manager.OfflinePlayerDataManager;
import org.bson.Document;

import java.util.UUID;

public class UUIDFinderByName {

    private final String name;
    private final Document document;

    public UUIDFinderByName(String name) throws CorePluginException {
        this.name = name.toLowerCase();
        document = OfflinePlayerDataManager.getColletion().find(getQuery()).first();
        if (document == null) throw new CorePluginException("User not found!");
    }

    public Document getDocument() {
        return document;
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
