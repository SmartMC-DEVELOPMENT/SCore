package us.smartmc.game;

import lombok.Getter;
import me.imsergioh.pluginsapi.handler.VariablesHandler;
import me.imsergioh.pluginsapi.instance.SpigotYmlConfig;
import me.imsergioh.pluginsapi.language.EnumMessagesRegistry;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import us.smartmc.backend.handler.ConnectionInputManager;
import us.smartmc.core.SmartCore;
import us.smartmc.game.listener.BackendBukkitListeners;
import us.smartmc.game.listener.PlayerRegistryListeners;
import us.smartmc.game.manager.ConfigsManager;
import us.smartmc.game.manager.IslandsSchematicsManager;
import us.smartmc.game.message.SkyBlockPlayerMesssages;
import us.smartmc.game.variable.SkyBlockPlayerVariables;
import us.smartmc.skyblock.ISkyBlockAPI;
import us.smartmc.skyblock.instance.SkyBlockServerType;

import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Level;

@Getter
public class SkyBlockPlugin extends JavaPlugin {

    @Getter
    private static SkyBlockPlugin plugin;
    private static ISkyBlockAPI api;

    private SpigotYmlConfig mainConfig;

    @Override
    public void onEnable() {
        plugin = this;
        ConfigsManager.load();
        setupAPI();

        registerListeners();
        registerServerToBackend();
        ConnectionInputManager.registerConnectionAction(c -> {
            SkyBlockPlugin.registerServerToBackend();
        });
        EnumMessagesRegistry.registerLanguageHolder(SkyBlockPlayerMesssages.class);

        VariablesHandler.register(new SkyBlockPlayerVariables());

        IslandsSchematicsManager.registerDefaults();
    }

    private static void setupAPI() {
        String configType = ConfigsManager.getConfig().getString("serverType");
        SkyBlockServerType type = SkyBlockServerType.valueOf(configType);
        Class<? extends ISkyBlockAPI> classType = null;

        switch (type) {
            case SPAWN -> classType = SkyBlockLobby.class;
            case ISLAND -> classType = SkyBlockIslands.class;
        }

        if (classType == null) {
            plugin.getLogger().log(Level.WARNING, "Error while trying to setup SkyBlock API (No classType found! " + configType + ")");
            Bukkit.getPluginManager().disablePlugin(plugin);
            return;
        }
        try {
            api = (ISkyBlockAPI) classType.getDeclaredConstructors()[0].newInstance();
            api.onEnable();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    public static void registerServerToBackend() {
        CompletableFuture.runAsync(() -> {
            String serverId = SmartCore.getServerId();
            SkyBlockServerType type = api.getBlockModeType();
            int count = Bukkit.getOnlinePlayers().size();
            sendBackendCommand("skyblock registerserver", serverId, type.name(), String.valueOf(count));
        });
    }

    private void registerListeners() {
        registerListeners(
                new PlayerRegistryListeners(),
                new BackendBukkitListeners());
    }

    @Override
    public void onDisable() {
        if (api != null) api.onDisable();
        sendBackendCommand("skyblock unregisterserver", SmartCore.getServerId());
    }

    public static void registerListeners(Listener... listeners) {
        for (Listener listener : listeners) {
            Bukkit.getPluginManager().registerEvents(listener, plugin);
        }
    }

    public static void sendBackendCommand(String... args) {
        StringBuilder stringBuilder = new StringBuilder();
        for (String arg : args) {
            stringBuilder.append(arg);
            stringBuilder.append(" ");
        }
        stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        String cmdLine = stringBuilder.toString();
        SmartCore.getPlugin().sendBackendCommand(cmdLine);
    }
}
