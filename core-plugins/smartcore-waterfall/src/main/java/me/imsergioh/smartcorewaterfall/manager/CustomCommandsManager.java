
package me.imsergioh.smartcorewaterfall.manager;

import lombok.Getter;
import me.imsergioh.pluginsapi.connection.MongoDBConnection;
import me.imsergioh.pluginsapi.instance.MongoDBPluginConfig;
import me.imsergioh.smartcorewaterfall.instance.commandmanager.CustomCommandExecutor;
import net.md_5.bungee.api.CommandSender;
import org.bson.Document;

import java.util.*;
import java.util.function.Consumer;

@Getter
public class CustomCommandsManager extends MongoDBPluginConfig {


    private static final Set<CustomCommandsManager> managers = new HashSet<>();
    private static final HashMap<String, CustomCommandExecutor> commands = new HashMap<>();

    private final String name;
    public CustomCommandsManager(String name, String database, String collection) {
        super(database, collection, new Document("_id", name));
        this.name = name;
        load();
        if (keySet().size() == 1 && containsKey("_id")) {
            put("/test_" + name, Arrays.asList("test", "test"));
        }
        managers.add(this);
        save();
    }

    public boolean execute(CommandSender sender, String message) {
        if (!containsLabel(message)) return false;

        boolean executed = false;
        for (String cmdLabel : new ArrayList<>(getList(message, String.class))) {
            if (!containsCommand(cmdLabel)) continue;
            executeCommand(sender, cmdLabel);
            executed = true;
        }
        return executed;
    }

    public boolean containsLabel(String message) {
        return containsKey(message);
    }

    public static boolean containsCommand(String label) {
        return commands.containsKey(getName(label));
    }

    public static void executeCommand(CommandSender sender, String label) {
        String name = getName(label);
        if (!containsCommand(label)) return;
        String[] args = getArgs(label);
        commands.get(name).onCommand(sender, args);
    }

    public static String getName(String label) {
        if (label.contains(" ")) return label.split(" ")[0];
        return label;
    }

    public static String[] getArgs(String label) {
        label = label.replaceFirst(getName(label) + " ", "");
        return label.split(" ");
    }

    public static void register(String name, CustomCommandExecutor executor) {
        commands.put(name, executor);
    }

    public static void forEach(Consumer<CustomCommandsManager> consumer) {
        managers.forEach(consumer);
    }

    public static void unregisterAll() {
        managers.forEach(managers::remove);
    }

    public static void load(String database, String collection) {
        for (Document document : MongoDBConnection.mainConnection.getDatabase(database).getCollection(collection)
                .find()) {
            new CustomCommandsManager(document.getString("_id"), database, collection);
        }
    }

}

