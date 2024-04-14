
package us.smartmc.smartcore.smartcorevelocity.manager;

import lombok.Getter;
import me.imsergioh.pluginsapi.connection.MongoDBConnection;
import me.imsergioh.pluginsapi.instance.MongoDBPluginConfig;
import org.bson.Document;

import java.util.*;
import java.util.function.Consumer;

@Getter
public class AllowedCommandsManager extends MongoDBPluginConfig {

    private static final String DATABASE = "proxy_data";
    private static final String COLLECTION = "allowed_commands";
    private static final Set<String> available = new HashSet<>();
    private static final Map<String, AllowedCommandsManager> managers = new HashMap<>();

    private final String name;

    public AllowedCommandsManager(String serverPrefix) {
        super(DATABASE, COLLECTION, getQuery(serverPrefix));
        this.name = serverPrefix;
        load();
    }

    public List<String> getAllowedCommands() {
        return getList("allowed", String.class);
    }

    public static AllowedCommandsManager get(String serverName) {
        for (String availableId : available) {
            if (serverName.startsWith(availableId)) {
                return getOrCreate(availableId);
            }
        }
        return null;
    }

    private static AllowedCommandsManager getOrCreate(String id) {
        AllowedCommandsManager manager = managers.get(id);
        if (manager != null) return manager;
        manager = new AllowedCommandsManager(id);
        managers.put(id, manager);
        return manager;
    }

    private static Document getQuery(String serverPrefix) {
        return new Document("_id", serverPrefix);
    }

    public static void loadAvailableServerIds() {
        for (Document document : MongoDBConnection.mainConnection.getDatabase(DATABASE).getCollection(COLLECTION).find()) {
            String id = document.getString("_id");
            System.out.println("CACHED ALLOWED_COMMAND ID OF " + id);
            available.add(id);
        }
    }

    public static void forEach(Consumer<AllowedCommandsManager> consumer) {
        managers.values().forEach(consumer);
    }

}

