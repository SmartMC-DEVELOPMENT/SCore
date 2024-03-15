package me.imsergioh.loginspigot.listener;

import me.imsergioh.loginspigot.instance.LoginPlayer;
import me.imsergioh.loginspigot.manager.LoginPlayersFactory;
import me.imsergioh.loginspigot.util.PluginUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.Timer;
import java.util.TimerTask;

public class AuthPlayersListeners implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void connect(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        LoginPlayer loginPlayer = LoginPlayersFactory.get(player);
        if (loginPlayer == null) return;

        // CHECK FOR EXECUTING ONLY ONCE
        if (loginPlayer.isCheck()) return;
        loginPlayer.setCheck(true);

        // IS PREMIUM USER = SET AUTH AUTO TO TRUE
        if (player.getUniqueId().version() == 4) {
            loginPlayer.setAuth(true);
        }

        if (!loginPlayer.isPremium()) {
            // CRACKED
            loginPlayer.checkSecretKey();
        } else {
            // PREMIUM
            PluginUtils.redirectTo(player, "lobby");
        }
    }
}
