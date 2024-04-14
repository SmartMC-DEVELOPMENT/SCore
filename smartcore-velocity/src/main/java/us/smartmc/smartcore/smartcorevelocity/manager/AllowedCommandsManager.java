
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

    public static List<AllowedCommandsManager> get(String serverName) {
        Set<AllowedCommandsManager> list = new HashSet<>();
        try {
            list.add(getOrLoad("global"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        for (String availableId : available) {
            if (serverName.startsWith(availableId)) {
                try {
                    list.add(getOrLoad(availableId));
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return list;
    }

    private static AllowedCommandsManager getOrLoad(String id) throws Exception {
        if (!available.contains(id)) throw new Exception("No found");
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
        available.add("global");
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

