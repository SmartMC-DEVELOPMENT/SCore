package us.smartmc.survivaladdon.fixescore.listener;

import me.imsergioh.pluginsapi.connection.RedisConnection;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import us.smartmc.smartaddons.plugin.AddonListener;

import java.util.Timer;
import java.util.TimerTask;

public class OnlineCountListener extends AddonListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        if (!isEnabled()) return;
        updateCount();
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        if (!isEnabled()) return;
        updateCount();
    }

    public void updateCount() {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                RedisConnection.mainConnection.getResource().set("online.survival", String.valueOf(Bukkit.getOnlinePlayers().size()));
            }
        }, 75);
    }

}
