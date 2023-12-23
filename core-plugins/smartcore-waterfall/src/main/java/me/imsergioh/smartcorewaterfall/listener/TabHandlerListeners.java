package me.imsergioh.smartcorewaterfall.listener;

import me.imsergioh.pluginsapi.language.Language;
import me.imsergioh.smartcorewaterfall.instance.PlayerLanguages;
import me.imsergioh.smartcorewaterfall.manager.TabHandler;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ServerConnectedEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class TabHandlerListeners implements Listener {

    @EventHandler
    public void send(ServerConnectedEvent event) {
        sendTab(event.getPlayer());
    }

    public static void sendTab(ProxiedPlayer player) {
        Language language = PlayerLanguages.getLanguage(player);
        TabHandler handler = TabHandler.getHandlers().get(language);
        String header = ChatColor.translateAlternateColorCodes('&', handler.getString("header"));
        String footer = ChatColor.translateAlternateColorCodes('&', handler.getString("footer"));
        player.setTabHeader(TextComponent.fromLegacyText(header),
                TextComponent.fromLegacyText(footer));
    }
}
