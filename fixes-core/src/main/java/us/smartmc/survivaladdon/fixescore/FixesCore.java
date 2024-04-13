package us.smartmc.survivaladdon.fixescore;

import lombok.Getter;
import me.imsergioh.pluginsapi.connection.RedisConnection;
import me.imsergioh.pluginsapi.handler.PubSubConnectionHandler;
import org.bukkit.Bukkit;
import us.smartmc.smartaddons.plugin.AddonInfo;
import us.smartmc.smartaddons.plugin.AddonPlugin;
import us.smartmc.smartaddons.spigot.SmartAddonsSpigot;
import us.smartmc.survivaladdon.fixescore.command.ExecuteAtBungeeCommand;
import us.smartmc.survivaladdon.fixescore.handler.ServerConnectionsHandler;
import us.smartmc.survivaladdon.fixescore.listener.OnlineCountListener;

@AddonInfo(name = "fixes-core")
public class FixesCore extends AddonPlugin {

    @Getter
    private static FixesCore plugin;

    @Override
    public void start() {
        plugin = this;

        new Thread(() -> {
            RedisConnection.mainConnection = new RedisConnection("localhost", 6379);
            PubSubConnectionHandler.register(new ServerConnectionsHandler());
        }).start();

        registerCommand(new ExecuteAtBungeeCommand());

        registerListeners(new OnlineCountListener());

        Bukkit.getServer().getMessenger().registerOutgoingPluginChannel(SmartAddonsSpigot.getPlugin(), "BungeeCord");
    }

    @Override
    public void stop() {

    }
}
