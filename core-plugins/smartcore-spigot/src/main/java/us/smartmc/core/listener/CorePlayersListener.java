package us.smartmc.core.listener;

import me.imsergioh.pluginsapi.event.PlayerDataLoadedEvent;
import me.imsergioh.pluginsapi.language.Language;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import us.smartmc.core.instance.player.SmartCorePlayer;
import us.smartmc.core.util.PluginUtils;

public class CorePlayersListener implements Listener {

    @EventHandler
    public void registerSmartCorePlayer(PlayerJoinEvent event)  {
        SmartCorePlayer.register(event.getPlayer());
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onDisconnect(PlayerQuitEvent event) {
        SmartCorePlayer.unload(event.getPlayer());
    }
}
