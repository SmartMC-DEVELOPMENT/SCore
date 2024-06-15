package us.smartmc.game;

import lombok.Getter;
import me.imsergioh.pluginsapi.instance.SpigotYmlConfig;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import us.smartmc.backend.connection.BackendClient;
import us.smartmc.backend.handler.ConnectionInputManager;
import us.smartmc.core.SmartCore;
import us.smartmc.game.backend.SendInfoListener;
import us.smartmc.game.listener.BackendBukkitListeners;
import us.smartmc.game.listener.PlayerRegistryListeners;
import us.smartmc.game.manager.ConfigsManager;
import us.smartmc.skyblock.ISkyBlockAPI;
import us.smartmc.skyblock.instance.SkyBlockServerType;

public class SkyBlockPlugin extends JavaPlugin implements ISkyBlockAPI {

    @Getter
    private static SkyBlockPlugin plugin;

    @Getter
    private SpigotYmlConfig mainConfig;

    @Override
    public void onEnable() {
        plugin = this;
        ConfigsManager.load();
        registerListeners();

        registerServerToBackend();
        ConnectionInputManager.registerListeners(new SendInfoListener());
    }

    public static void registerServerToBackend() {
        String serverId = SmartCore.getServerID();
        SkyBlockServerType type = plugin.getBlockModeType();
        int count = Bukkit.getOnlinePlayers().size();
        sendBackendCommand("skyblock registerserver", serverId, type.name(), String.valueOf(count));
    }

    private void registerListeners() {
        registerListeners(
                new PlayerRegistryListeners(),
                new BackendBukkitListeners());
    }

    @Override
    public void onDisable() {
        sendBackendCommand("skyblock unregisterserver", SmartCore.getServerID());
    }

    @Override
    public SkyBlockServerType getBlockModeType() {
        return SkyBlockServerType.valueOf(ConfigsManager.getConfig().getString("serverType"));
    }

    private static void registerListeners(Listener... listeners) {
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
        BackendClient.mainConnection.sendCommand(cmdLine);
    }

}
