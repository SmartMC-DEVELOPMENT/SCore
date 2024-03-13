package us.smartmc.smartcore.smartcorevelocity.listener;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.PluginMessageEvent;
import com.velocitypowered.api.proxy.Player;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class LoginMessagingListeners {

    private static final Set<UUID> loggedInUsers = new HashSet<>();

    @Subscribe
    public void onPluginMessage(PluginMessageEvent event) {
        if (!event.getIdentifier().getId().equalsIgnoreCase("smartlogin")) {
            return;
        }
        if (!(event.getTarget() instanceof Player player)) return;
        DataInputStream in = new DataInputStream(new ByteArrayInputStream(event.getData()));
        try {
            String subchannel = in.readUTF();
            if (subchannel.equals("login")) {
                loggedInUsers.add(player.getUniqueId());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean isLoggedIn(Player player) {
        return loggedInUsers.contains(player.getUniqueId());
    }

}
