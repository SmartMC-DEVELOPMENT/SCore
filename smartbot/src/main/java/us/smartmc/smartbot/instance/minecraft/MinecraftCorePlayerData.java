package us.smartmc.smartbot.instance.minecraft;

import com.mongodb.client.MongoCollection;
import me.imsergioh.pluginsapi.language.IMessageCategory;
import me.imsergioh.pluginsapi.language.LangMessagesInfo;
import me.imsergioh.pluginsapi.language.Language;
import net.dv8tion.jda.api.entities.Member;
import org.bson.Document;
import us.smartmc.backend.connection.BackendClient;
import us.smartmc.smartbot.util.MinecraftLinkUtil;

import java.text.MessageFormat;
import java.util.*;

public class MinecraftCorePlayerData {

    private final UUID id;
    private final Document document;

    public MinecraftCorePlayerData(UUID id) {
        this.id = id;
        this.document = loadDocumentData();
        System.out.println("MinecraftCorePlayerData? " + document);
    }

    public Language getLanguage() {
        return Language.valueOf(document.getString("lang"));
    }

    public long getFirstLogin() {
        return document.getLong("firstLogin");
    }

    public long getLastLogin() {
        return document.getLong("lastLogin");
    }

    public long getSmartCoins() {
        return document.get("coins", Number.class).longValue();
    }

    public boolean hasFlyEnabled() {
        return document.getBoolean("fly", false);
    }

    private Document loadDocumentData() {
        MongoCollection<Document> collection = MinecraftLinkUtil.getCorePlayerDataCollection();
        Document dbDocument = collection.find(getQuery(id)).first();
        if (dbDocument == null) dbDocument = getQuery(id);
        return dbDocument;
    }

    private static Document getQuery(UUID id) {
        return new Document("_id", id.toString());
    }

    public static MinecraftCorePlayerData get(UUID id) {
        return new MinecraftCorePlayerData(id);
    }
}
