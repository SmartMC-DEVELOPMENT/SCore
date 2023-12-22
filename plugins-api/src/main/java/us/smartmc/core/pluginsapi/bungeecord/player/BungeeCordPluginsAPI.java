package us.smartmc.core.pluginsapi.bungeecord.player;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;

public class BungeeCordPluginsAPI {

    private static Plugin plugin;
    private static ProxyServer server;

    public static void register(Plugin apiPlugin) {
        plugin = apiPlugin;
        server = plugin.getProxy();
    }

    public static ProxyServer getServer() {
        return server;
    }

    public static Plugin getPlugin() {
        return plugin;
    }
}
