package us.smartmc.smartcore.smartcorevelocity.listener;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.player.ServerConnectedEvent;
import com.velocitypowered.api.event.player.TabCompleteEvent;
import com.velocitypowered.api.proxy.Player;
import me.imsergioh.pluginsapi.language.Language;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import us.smartmc.smartcore.smartcorevelocity.instance.PlayerLanguages;
import us.smartmc.smartcore.smartcorevelocity.manager.TabHandler;

public class TabHandlerListeners {

    @Subscribe
    public void send(ServerConnectedEvent event) {
        sendTab(event.getPlayer());
    }

    public static void sendTab(Player player) {
        Language language = PlayerLanguages.getLanguage(player);
        TabHandler handler = TabHandler.getHandlers().get(language);
        String header = handler.getString("header").replace("&", "§");
        String footer = handler.getString("footer").replace("&", "§");

        Component headerComponent = LegacyComponentSerializer.legacySection().deserialize(header);
        Component footerComponent = LegacyComponentSerializer.legacySection().deserialize(footer);

        player.sendPlayerListHeaderAndFooter(headerComponent, footerComponent);
    }
}
