package us.smartmc.core.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;
import us.smartmc.core.instance.player.SmartCorePlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import us.smartmc.core.pluginsapi.handler.MenusManager;
import us.smartmc.core.pluginsapi.language.Language;
import us.smartmc.core.pluginsapi.spigot.event.PlayerDataLoadedEvent;

public class CorePlayersListener implements Listener {

    @EventHandler
    public void registerSmartCorePlayer(PlayerJoinEvent event)  {
        SmartCorePlayer.register(event.getPlayer().getUniqueId());
    }

    @EventHandler
    public void onDisconnect(PlayerQuitEvent event) {
        SmartCorePlayer.unload(event.getPlayer());
    }

    @EventHandler
    public void onLoad(PlayerDataLoadedEvent event) {
        event.getData().registerData("lang", Language.getDefault().name());
        event.getData().registerData(SmartCorePlayer.COINS_PATH, 0);
    }
}
