
package us.smartmc.snowgames.manager;

import lombok.Getter;
import me.imsergioh.pluginsapi.handler.LanguagesHandler;
import me.imsergioh.pluginsapi.instance.manager.PluginManager;
import me.imsergioh.pluginsapi.language.Language;
import me.imsergioh.pluginsapi.language.MultiLanguageRegistry;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import us.smartmc.snowgames.FFAPlugin;
import us.smartmc.snowgames.messages.PluginMessages;
import us.smartmc.snowgames.object.MongoCollectionTop;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
public class TopsManager implements PluginManager {

    private final String DB_NAME, COLLECTION_NAME;

    private final Map<String, MongoCollectionTop> tops = new HashMap<>();
    private final Map<String, List<Document>> cachedTops = new HashMap<>();

    private long lastUpdate;

    public TopsManager(String dbName, String collectionName) {
        this.DB_NAME = dbName;
        this.COLLECTION_NAME = collectionName;
    }

    public TopsManager register(String... documentPaths) {
        for (String path : documentPaths) {
            tops.put(path, new MongoCollectionTop(DB_NAME, COLLECTION_NAME, path));
            for (Language language : Language.values()) {
                LanguagesHandler.get(language).get(PluginMessages.NAME).registerDefault("top_" + path + "_name", path);
            }
        }

        for (Language language : Language.values()) {
            LanguagesHandler.get(language).get(PluginMessages.NAME).save();
        }

        return this;
    }

    @Override
    public void load() {
        update();
    }

    @Override
    public void unload() {
    }

    public long getPlayerRank(String name, Player player) {
        MongoCollectionTop top = tops.get(name);
        return top.getPlayerRank(player.getUniqueId().toString());
    }

    public Document getTop(String name, int index) {
        update();
        MongoCollectionTop top = tops.get(name);
        List<Document> documents = cachedTops.get(name);
        if (documents.isEmpty()) return null;
        if (documents.size() <= index) return null;
        Document document = documents.get(index);
        String playerName = document.getString("name");
        Object value = document.get(top.getDocumentPath());
        return new Document().append("name", playerName).append("value", value);
    }

    public void update() {
        long currentMillis = System.currentTimeMillis();
        if ((currentMillis - lastUpdate) < (10 * 1000)) return;
        lastUpdate = System.currentTimeMillis();
        Bukkit.getScheduler().runTaskAsynchronously(FFAPlugin.getFFAPlugin(), () -> {
            for (String name : tops.keySet()) {
                MongoCollectionTop top = tops.get(name);
                cachedTops.put(name, top.getTopScores());
            }
        });
    }

    public MongoCollectionTop get(String name) {
        return tops.get(name);
    }

}