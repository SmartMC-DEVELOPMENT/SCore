package us.smartmc.game.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import us.smartmc.game.instance.SkyBlockPlayer;
import us.smartmc.skyblock.manager.PlayersManager;

public class PlayerRegistryListeners implements Listener {

    @EventHandler
    public void registerPluginPlayer(PlayerJoinEvent event) {
        PlayersManager.register(SkyBlockPlayer.getOrCreate(event.getPlayer()));
    }
}
