package us.smartmc.game.luckytowers.listener;

import me.imsergioh.pluginsapi.event.PlayerDataLoadedEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import us.smartmc.game.luckytowers.LuckyTowers;
import us.smartmc.game.luckytowers.instance.player.GamePlayer;
import us.smartmc.game.luckytowers.manager.EditorModeManager;
import us.smartmc.game.luckytowers.menu.LobbyHotbar;

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

    @EventHandler
    public void removeEditorMode(PlayerQuitEvent event) {
        EditorModeManager.disable(event.getPlayer());
    }

    @EventHandler
    public void giveLobbyHotbar(PlayerDataLoadedEvent event) {
        new LobbyHotbar(event.getPlayer()).set(event.getPlayer());
    }
}
