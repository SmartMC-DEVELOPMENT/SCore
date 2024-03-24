package us.smartmc.smartcore.smartcorevelocity.listener;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.DisconnectEvent;
import com.velocitypowered.api.event.player.ServerConnectedEvent;
import com.velocitypowered.api.proxy.Player;
import com.viaversion.viaversion.api.Via;
import me.imsergioh.pluginsapi.connection.RedisConnection;
import me.imsergioh.pluginsapi.language.Language;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import us.smartmc.smartcore.smartcorevelocity.instance.PlayerLanguages;
import us.smartmc.smartcore.smartcorevelocity.manager.TabHandler;

public class PlayerVersionListeners {

    @Subscribe
    public void publish(ServerConnectedEvent event) {
        RedisConnection.mainConnection.getResource().set("playerVersion." + event.getPlayer().getUniqueId(),
                String.valueOf(Via.getAPI().getPlayerVersion(event.getPlayer().getUniqueId())));
    }

    @Subscribe
    public void removeCache(DisconnectEvent event) {
        RedisConnection.mainConnection.getResource().del("playerVersion." + event.getPlayer().getUniqueId());
    }
}
