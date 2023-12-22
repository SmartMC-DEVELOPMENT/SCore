package us.smartmc.core.pluginsapi.spigot.player;

import com.mongodb.client.MongoCollection;
import us.smartmc.core.pluginsapi.connection.MongoDBConnection;
import us.smartmc.core.pluginsapi.instance.PlayerLanguages;
import us.smartmc.core.pluginsapi.language.Language;
import us.smartmc.core.pluginsapi.spigot.event.FirstCorePlayerJoinEvent;
import us.smartmc.core.pluginsapi.spigot.event.PlayerDataLoadedEvent;
import org.bson.Document;
import org.bukkit.Bukkit;
import us.smartmc.core.pluginsapi.util.SyncUtil;

public class CorePlayerData {

    private final CorePlayer corePlayer;

    private Document document;

    public CorePlayerData(CorePlayer corePlayer) {
        this.corePlayer = corePlayer;
        document = new Document("_id", corePlayer.getUUID().toString());
        loadData();
        registerData("firstLogin", System.currentTimeMillis());
        document.put("lastLogin", System.currentTimeMillis());
    }

    public void registerData(String path, Object value) {
        if (document.containsKey(path)) return;
        document.put(path, value);
    }

    public void setData(String path, Object value) {
        document.put(path, value);
    }

    private void loadData() {
        // QUERY DOCUMENT MONGODB AND SET IF FOUND
        Document queryDocument = document;
        Document document = MongoDBConnection.mainConnection
                .getDatabase("player_data")
                .getCollection("core_players").find(queryDocument).first();
        if (document != null) {
            this.document = document;
        } else {
            // Document not found: Calls first core player join event
            Bukkit.getPluginManager().callEvent(new FirstCorePlayerJoinEvent(corePlayer));
        }

        // Register language
        String langString = this.document.getString("lang");
        Language language = null;
        try {
            language = Language.valueOf(langString);
        } catch (Exception e) {
            language = Language.getDefault();
            this.document.put("lang", language.name());
        }
        PlayerLanguages.register(corePlayer.getUUID(), language);

        registerData("lang", PlayerLanguages.get(corePlayer.getUUID()).name());

        SyncUtil.sync(() -> {
            Bukkit.getPluginManager().callEvent(new PlayerDataLoadedEvent(corePlayer, this));
        });
    }


    public void save() {
        // GET COLLECTION
        MongoCollection<Document> collection = MongoDBConnection.mainConnection.getDatabase("player_data")
                .getCollection("core_players");

        // FILTER DOCUMENT (CREATE)
        Document queryDocument = new Document("_id", corePlayer.getUUID().toString());

        // DELETE
        collection.deleteMany(queryDocument);

        // INSERT DOCUMENT AGAIN
        collection.insertOne(document);
    }

    public Document getDocument() {
        return document;
    }

    public CorePlayer getCorePlayer() {
        return corePlayer;
    }
}
