package me.imsergioh.smartcorewaterfall.listener;

import me.imsergioh.pluginsapi.instance.PlayerLanguages;
import me.imsergioh.pluginsapi.util.ChatUtil;
import me.imsergioh.smartcorewaterfall.SmartCoreWaterfall;
import me.imsergioh.smartcorewaterfall.messages.ProxyMainMessages;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BungeeMessagingListeners implements Listener {

    @EventHandler
    public void onPluginMessage(PluginMessageEvent event) {
        if (!event.getTag().equalsIgnoreCase("BungeeCord")) {
            return;
        }

        DataInputStream in = new DataInputStream(new ByteArrayInputStream(event.getData()));

        try {
            String subchannel = in.readUTF();
            if (subchannel.equals("RedirectTo")) {
                String serverPrefix = in.readUTF();
                if (event.getReceiver() instanceof ProxiedPlayer player) {
                    List<ServerInfo> list = new ArrayList<>();
                    for (ServerInfo info : SmartCoreWaterfall.getPlugin().getProxy().getServersCopy().values()) {
                        if (!info.getName().startsWith(serverPrefix)) continue;
                        list.add(info);
                    }
                    if (list.isEmpty()) {
                        String message = ProxyMainMessages.get(PlayerLanguages.get(player.getUniqueId()), "servers_not_found");
                        player.sendMessage(new TextComponent(ChatUtil.parse(message)));
                        return;
                    }
                    // Aquí manejas la redirección
                    ServerInfo target = list.get(new Random().nextInt(list.size()));
                    if (target != null) {
                        player.connect(target);
                    } else {
                        player.sendMessage(new TextComponent(ChatColor.RED + "Servers not found."));
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
