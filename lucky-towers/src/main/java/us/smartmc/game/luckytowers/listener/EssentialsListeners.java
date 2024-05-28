package us.smartmc.game.luckytowers.listener;

import me.imsergioh.pluginsapi.event.PlayerDataLoadedEvent;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import us.smartmc.game.luckytowers.LuckyTowers;
import us.smartmc.game.luckytowers.instance.game.GameSessionStatus;
import us.smartmc.game.luckytowers.instance.player.GamePlayer;
import us.smartmc.game.luckytowers.instance.player.PlayerStatus;
import us.smartmc.game.luckytowers.manager.EditorModeManager;

public class EssentialsListeners implements Listener {

    @EventHandler
    public void removeJoinMessage(PlayerJoinEvent event) {
        event.joinMessage(null);
    }

    @EventHandler
    public void removeQuitMessage(PlayerQuitEvent event) {
        event.quitMessage(null);
    }

    @EventHandler
    public void createGamePlayer(PlayerDataLoadedEvent event) {
        GamePlayer.get(event.getPlayer().getUniqueId());
    }

    @EventHandler
    public void removeEditorMode(PlayerQuitEvent event) {
        EditorModeManager manager = LuckyTowers.getManager(EditorModeManager.class);
        manager.disable(event.getPlayer());
    }

    @EventHandler
    public void cancelDamageIfNotInGame(EntityDamageEvent event) {
        Entity entity = event.getEntity();

        if (!(entity instanceof Player player)) return;
        GamePlayer gamePlayer = GamePlayer.get(player.getUniqueId());
        if (gamePlayer == null) {
            event.setCancelled(true);
            return;
        }

        // Cancel damage event if is not ingame and not playing
        event.setCancelled(!(gamePlayer.getStatus().equals(PlayerStatus.INGAME) && gamePlayer.getGameSession().getStatus().equals(GameSessionStatus.PLAYING)));
    }
}
