package us.smartmc.smartcore.proxy.listener;

import me.imsergioh.pluginsapi.instance.handler.RedisPubSubListener;
import me.imsergioh.pluginsapi.manager.BungeeCordPluginsAPI;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import us.smartmc.smartcore.proxy.event.LoggedInProxyPlayerEvent;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class LoginMessageHandler extends RedisPubSubListener {

    private static final Set<UUID> loggedInUsers = new HashSet<>();

    public LoginMessageHandler() {
        super("login");
    }

    public static void unregisterLoggedIn(ProxiedPlayer player) {
        loggedInUsers.remove(player.getUniqueId());
    }

    public static boolean isLoggedIn(ProxiedPlayer player) {
        return loggedInUsers.contains(player.getUniqueId());
    }

    @Override
    public void onMessage(String s) {
        UUID uuid = UUID.fromString(s);
        ProxiedPlayer player = BungeeCordPluginsAPI.proxy.getPlayer(uuid);

        loggedInUsers.add(uuid);
        BungeeCordPluginsAPI.proxy.getPluginManager().callEvent(new LoggedInProxyPlayerEvent(player));
    }
}
