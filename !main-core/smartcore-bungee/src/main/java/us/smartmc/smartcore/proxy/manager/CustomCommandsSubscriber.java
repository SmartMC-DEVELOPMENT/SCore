
package us.smartmc.smartcore.proxy.manager;

import me.imsergioh.pluginsapi.instance.handler.RedisPubSubListener;
import us.smartmc.smartcore.proxy.SmartCoreBungeeCord;

import java.util.logging.Level;

public class CustomCommandsSubscriber extends RedisPubSubListener {

    private static final SmartCoreBungeeCord plugin = SmartCoreBungeeCord.getPlugin();

    public CustomCommandsSubscriber() {
        super("proxy@custom_cmds");
    }

    @Override
    public void onMessage(String message) {
        if (message.equals("reload")) {
            CustomCommandsManager.unregisterAll();
            plugin.loadCustomCommands();
            CustomCommandsManager.forEach(manager -> {
                manager.load();
                plugin.getLogger().log(Level.INFO, "CustomCommands recargados: " + manager.getName());
            });

            AllowedCommandsManager.forEach(manager -> {
                manager.load();
                plugin.getLogger().log(Level.INFO, "AllowedCommands recargados: " + manager.getName());
            });
        }

        if (message.startsWith("removeCommandHandler")) {
            String id = message.split(" ")[1];
            CustomCommandsManager.unregister(id).forEach(name -> {
                plugin.getLogger().log(Level.INFO, "CustomCommands eliminados: " + name);
            });
        }

    }
}

