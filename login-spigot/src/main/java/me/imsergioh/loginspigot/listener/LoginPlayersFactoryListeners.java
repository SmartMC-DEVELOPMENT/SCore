package me.imsergioh.loginspigot.listener;

import me.imsergioh.loginspigot.manager.LoginPlayersFactory;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class LoginPlayersFactoryListeners implements Listener {


    @EventHandler(priority = EventPriority.LOWEST)
    public void disconnect(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        LoginPlayersFactory.unload(player.getUniqueId());
    }
}
