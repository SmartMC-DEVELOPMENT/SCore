package us.smartmc.smartcore.proxy.listener;

import me.imsergioh.pluginsapi.instance.PlayerLanguages;
import me.imsergioh.pluginsapi.manager.BungeeCordPluginsAPI;
import me.imsergioh.pluginsapi.util.ChatUtil;
import net.kyori.adventure.text.format.NamedTextColor;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.event.EventHandler;
import us.smartmc.smartcore.proxy.messages.ProxyMainMessages;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BungeeMessagingListeners {

    @EventHandler
    public void onPluginMessage(PluginMessageEvent event) {
        if (!event.getTag().equalsIgnoreCase("BungeeCord")) {
            return;
        }
        if (!(event.getSender() instanceof ProxiedPlayer player)) return;

        DataInputStream in = new DataInputStream(new ByteArrayInputStream(event.getData()));

        try {
            String subchannel = in.readUTF();

            if (subchannel.equals("RedirectTo")) {
                String serverPrefix = in.readUTF();

                if (player == null) return;

                List<ServerInfo> list = new ArrayList<>();
                for (ServerInfo server : BungeeCordPluginsAPI.proxy.getServers().values()) {
                    if (!server.getName().startsWith(serverPrefix)) continue;
                    list.add(server);
                }
                if (list.isEmpty()) {
                    String message = ProxyMainMessages.get(PlayerLanguages.get(player.getUniqueId()), "servers_not_found");
                    player.sendMessage(ChatUtil.parse(message));
                    return;
                }

                ServerInfo target = list.get(new Random().nextInt(list.size()));
                // Aquí manejas la redirección
                if (target != null) {
                    player.connect(target);
                } else {
                    player.sendMessage(new TextComponent(NamedTextColor.RED + "Servers not found."));
                }

            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
