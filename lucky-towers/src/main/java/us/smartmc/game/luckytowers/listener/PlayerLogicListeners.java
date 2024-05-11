package us.smartmc.game.luckytowers.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import us.smartmc.game.luckytowers.config.MainPluginConfig;
import us.smartmc.game.luckytowers.event.GameStatusChangeEvent;
import us.smartmc.game.luckytowers.event.player.GamePlayerJoinSessionEvent;
import us.smartmc.game.luckytowers.event.player.PlayerStatusChangeEvent;
import us.smartmc.game.luckytowers.instance.game.GameSession;
import us.smartmc.game.luckytowers.instance.game.GameSessionStatus;
import us.smartmc.game.luckytowers.instance.player.PlayerStatus;
import us.smartmc.game.luckytowers.menu.hotbar.SpectatorHotbar;
import us.smartmc.game.luckytowers.menu.hotbar.WaitingHotbar;
import us.smartmc.game.luckytowers.util.GameUtil;

public class PlayerLogicListeners implements Listener {

    @EventHandler
    public void teleportToLobby(PlayerStatusChangeEvent event) {
        if (event.isCancelled()) return;
        if (!(event.getStatus().equals(PlayerStatus.LOBBY) && MainPluginConfig.isLobbyEnabled())) return;

        // Teleport to lobby if gameplayer is online (Should xd)
        event.getGamePlayer().onlinePlayer(player -> player.teleport(MainPluginConfig.getLobby()));
    }

    @EventHandler
    public void updateScoreboard(PlayerStatusChangeEvent event) {
        GameUtil.updateScoreboard(event.getGamePlayer());
    }

    @EventHandler
    public void clearInventory(GameStatusChangeEvent event) {
        GameSession session = event.getSession();
        if (session == null) return;
        if (!(session.getStatus().equals(GameSessionStatus.STARTING))) return;
        session.forEachOnlinePlayer(p -> p.getInventory().clear());
    }

    @EventHandler
    public void giveWaitingHotbar(GamePlayerJoinSessionEvent event) {
        new WaitingHotbar(event.getPlayer()).set(event.getPlayer());
    }

    @EventHandler
    public void giveSpectatorHotbar(PlayerStatusChangeEvent event) {
        if (!(event.getStatus().equals(PlayerStatus.SPECTATING))) return;
        new SpectatorHotbar(event.getPlayer()).set(event.getPlayer());
    }
}
