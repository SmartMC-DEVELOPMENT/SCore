package me.imsergioh.smartcorewaterfall.manager;

import me.imsergioh.pluginsapi.instance.MongoDBPluginConfig;
import me.imsergioh.smartcorewaterfall.SmartCoreWaterfall;
import me.imsergioh.smartcorewaterfall.instance.onlinestore.OnlineStoreCommandType;
import org.bson.Document;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TebexCommandsManager {

    private final MongoDBPluginConfig config = new MongoDBPluginConfig("tebex", "commands", new Document("_id", "main"));
    private final TebexPackageManager packageManager;


    public TebexCommandsManager(TebexPackageManager packageManager) {
        this.packageManager = packageManager;
        load();
    }

    public void load() {
        config.load();
        registerPackagesDefaults();
    }

    public void performPackageCommands(String id, OnlineStoreCommandType type, Object... args) {
        Document document = config.get(id, Document.class);
        for (String command : new ArrayList<>(document.getList(type.getCommandSectionName(), String.class))) {
            // Format command label:
            if (command.contains(" ")) {
                for (String arg : command.split(" ")) {
                    if (arg.startsWith("{") && arg.endsWith("}")) {
                        String pathToGet = arg.replace("{", "").replace("}", "");
                        if (pathToGet.length() > 1)
                            command = command.replace(arg, document.getString(pathToGet));
                    }
                }
            }
            command = MessageFormat.format(command, args);
            System.out.println("Final command= " + command);
            SmartCoreWaterfall.getPlugin().getProxy().getPluginManager().dispatchCommand(
                    SmartCoreWaterfall.getPlugin().getProxy().getConsole(),
                    command
            );
        }
    }

    private void registerPackagesDefaults() {
        for (String packageId : packageManager.getPackageIds()) {
            Document document = packageManager.getPackageDocument(packageId);
            registerPackageDefaults(packageId, document.getString("name"));
        }
        config.save();
    }

    public void registerPackageDefaults(String id, String name) {
        Document document = new Document()
                .append("name", name)
                .append("luckperms_group_name", "default")
                .append("expiry_time", "1month")
                .append("purchase_commands", Arrays.asList("lpb user {0} parent addtemp {luckperms_group_name} {expiry_time}", "announcePackagePurchase {0} {name}"));
        config.registerDefault(id, document);
    }

}
