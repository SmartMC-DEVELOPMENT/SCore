package us.smartmc.smartcore.smartcorevelocity.listener;

import com.velocitypowered.api.proxy.Player;
import me.imsergioh.pluginsapi.instance.handler.RedisPubSubListener;
import me.imsergioh.pluginsapi.manager.VelocityPluginsAPI;
import us.smartmc.smartcore.smartcorevelocity.event.LoggedInProxyPlayerEvent;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public class LoginMessageHandler extends RedisPubSubListener {

    private static final Set<UUID> loggedInUsers = new HashSet<>();

    public LoginMessageHandler() {
        super("login");
    }

    public static void unregisterLoggedIn(Player player) {
        loggedInUsers.remove(player.getUniqueId());
    }

    public static boolean isLoggedIn(Player player) {
        return loggedInUsers.contains(player.getUniqueId());
    }

    @Override
    public void onMessage(String s) {
        UUID uuid = UUID.fromString(s);
        Optional<Player> playerOptional = VelocityPluginsAPI.proxy.getPlayer(uuid);
        if (playerOptional.isEmpty()) return;
        Player player = playerOptional.get();

        loggedInUsers.add(uuid);
        VelocityPluginsAPI.proxy.getEventManager().fireAndForget(new LoggedInProxyPlayerEvent(player));
    }
}
