package us.smartmc.core.luckywars.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import us.smartmc.core.luckywars.LuckyTowers;
import us.smartmc.core.luckywars.instance.player.GamePlayer;

public class EssentialsListeners implements Listener {

    private static final LuckyTowers plugin = LuckyTowers.getPlugin();

    @EventHandler
    public void removeJoinMessage(PlayerJoinEvent event) {
        event.joinMessage(null);
    }

    @EventHandler
    public void removeQuitMessage(PlayerQuitEvent event) {
        event.quitMessage(null);
    }

    @EventHandler
    public void createGamePlayer(PlayerJoinEvent event) {
        GamePlayer.get(event.getPlayer().getUniqueId());
    }
}
