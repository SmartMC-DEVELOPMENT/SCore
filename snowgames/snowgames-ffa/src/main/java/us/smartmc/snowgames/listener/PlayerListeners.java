package us.smartmc.snowgames.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import us.smartmc.gamesmanager.player.GamePlayer;
import us.smartmc.gamesmanager.player.GamePlayerRepository;
import us.smartmc.snowgames.FFAPlugin;
import us.smartmc.snowgames.inventory.LobbyHotbar;
import us.smartmc.snowgames.manager.ItemCooldownManager;
import us.smartmc.snowgames.player.FFAPlayer;
import us.smartmc.snowgames.util.GameItemUtils;

public class PlayerListeners implements Listener {

    @EventHandler
    public void giveLobbyHotbar(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        LobbyHotbar.give(player);
    }

    @EventHandler
    public void quitPlayer(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        GamePlayer gamePlayer = GamePlayerRepository.provide(FFAPlayer.class, player);
        FFAPlugin.getGame().quitPlayer(gamePlayer);
        GamePlayerRepository.remove(player.getUniqueId());
        ItemCooldownManager.clear(player);
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        event.getDrops().clear();
        event.setDeathMessage("");
        event.setKeepInventory(false);
        FFAPlayer ffaPlayer = GamePlayerRepository.provide(FFAPlayer.class, event.getEntity());
        FFAPlugin.getGame().deathPlayer(ffaPlayer);
    }

    @EventHandler
    public void onKill(PlayerDeathEvent event) {
        Player player = event.getEntity();
        Player killer = player.getKiller();
        if (killer == null) return;

        GameItemUtils.handlePlayerKill(killer);
    }
}
