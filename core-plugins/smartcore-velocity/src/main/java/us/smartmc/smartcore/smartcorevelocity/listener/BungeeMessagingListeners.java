package us.smartmc.smartcore.smartcorevelocity.listener;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.PluginMessageEvent;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import me.imsergioh.pluginsapi.instance.PlayerLanguages;
import me.imsergioh.pluginsapi.util.ChatUtil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import us.smartmc.smartcore.smartcorevelocity.messages.ProxyMainMessages;
import us.smartmc.smartcore.velocitycore.manager.VelocityPluginsAPI;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class BungeeMessagingListeners {

    @Subscribe
    public void onPluginMessage(PluginMessageEvent event) {
        if (!event.getIdentifier().getId().equalsIgnoreCase("BungeeCord")) {
            return;
        }

        if (!(event.getTarget() instanceof Player)) return;

        DataInputStream in = new DataInputStream(new ByteArrayInputStream(event.getData()));

        try {
            String subchannel = in.readUTF();

            if (subchannel.equals("RedirectTo")) {
                String serverPrefix = in.readUTF();

                Player player = (Player) event.getTarget();

                if (player == null) return;

                List<RegisteredServer> list = new ArrayList<>();
                for (RegisteredServer server : VelocityPluginsAPI.proxy.getAllServers()) {
                    if (!server.getServerInfo().getName().startsWith(serverPrefix)) continue;
                    list.add(server);
                }
                if (list.isEmpty()) {
                    String message = ProxyMainMessages.get(PlayerLanguages.get(player.getUniqueId()), "servers_not_found");
                    player.sendMessage(Component.text(ChatUtil.parse(message)));
                    return;
                }

                RegisteredServer target = list.get(new Random().nextInt(list.size()));
                // Aquí manejas la redirección
                if (target != null) {
                    player.createConnectionRequest(target).fireAndForget();
                } else {
                    player.sendMessage(Component.text(NamedTextColor.RED + "Servers not found."));
                }

            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
