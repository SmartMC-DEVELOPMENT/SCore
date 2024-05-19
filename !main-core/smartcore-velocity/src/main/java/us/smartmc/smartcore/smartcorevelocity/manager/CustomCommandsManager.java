
package us.smartmc.smartcore.smartcorevelocity.manager;

import com.velocitypowered.api.command.CommandSource;
import lombok.Getter;
import me.imsergioh.pluginsapi.connection.MongoDBConnection;
import me.imsergioh.pluginsapi.instance.MongoDBPluginConfig;
import org.bson.Document;
import us.smartmc.smartcore.smartcorevelocity.instance.commandmanager.CustomCommandExecutor;

import java.util.*;
import java.util.function.Consumer;

@Getter
public class CustomCommandsManager extends MongoDBPluginConfig {


    @Getter
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

    public boolean execute(CommandSource sender, String label) {
        boolean executedAtLeastOne = false;
        for (String literalCommands : getLiteralCommandsOfCommand(label)) {
            executeCommand(sender, literalCommands);
            executedAtLeastOne = true;
        }
        if (executedAtLeastOne) return executedAtLeastOne;

        for (String nameCommands : getCommandsOfCommandName(label)) {
            executeCommand(sender, nameCommands);
            executedAtLeastOne = true;
        }
        return executedAtLeastOne;
    }

    public List<String> getLiteralCommandsOfCommand(String label) {
        List<String> commands = new ArrayList<>();
        for (String key : keySet()) {
            if (key.contains(",")) {
                for (String alias : key.split(",")) {
                    if (alias.equalsIgnoreCase(label)) commands.addAll(getList(key, String.class));
                }
            } else if (key.equalsIgnoreCase(label)) commands.addAll(getList(key, String.class));
        }
        return commands;
    }

    public List<String> getCommandsOfCommandName(String label) {
        return getLiteralCommandsOfCommand(getName(label));
    }

    public boolean containsCommand(String label) {
        String name = getName(label);
        for (String key : keySet()) {
            String keyName = key.contains(" ") ? key.split(" ")[0] : key;
            if (key.contains(",")) {
                for (String alias : key.split(",")) {
                    String aliasName = alias.contains(" ") ? alias.split(" ")[0] : alias;
                    if (aliasName.equalsIgnoreCase(name)) return true;
                }
            } else if (keyName.equalsIgnoreCase(name)) return true;
        }
        return false;
    }

    public boolean containsLabel(String message) {
        return containsKey(message);
    }

    public static void executeCommand(CommandSource sender, String label) {
        String name = getName(label);
        if (!commands.containsKey(name)) return;
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

    public Collection<String> getCommandNames() {
        Set<String> list = new HashSet<>();
        for (String key : keySet()) {
            if (key.contains(",")) {
                for (String alias : key.split(",")) {
                    String aliasName = alias.contains(" ") ? alias.split(" ")[0] : alias;
                    list.add(aliasName);
                }
            } else list.add(key);
        }
        return list;
    }

    public static Collection<String> unregister(String name) {
        Set<String> list = new HashSet<>();
        new HashSet<>(managers).stream().filter(manager -> manager.getName().equals(name)).forEach(manager -> {
            for (String commandName : manager.getCommandNames()) {
                commands.remove(commandName);
            }
            managers.remove(manager);
            list.add(manager.getName());
        });
        return list;
    }

    public static void register(String name, CustomCommandExecutor executor) {
        commands.put(name, executor);
    }

    public static void forEach(Consumer<CustomCommandsManager> consumer) {
        managers.forEach(consumer);
    }

    public static void unregisterAll() {
        new HashSet<>(managers).forEach(managers::remove);
    }

    public static void load(String database, String collection) {
        for (Document document : MongoDBConnection.mainConnection.getDatabase(database).getCollection(collection)
                .find()) {
            new CustomCommandsManager(document.getString("_id"), database, collection);
        }
    }

}

