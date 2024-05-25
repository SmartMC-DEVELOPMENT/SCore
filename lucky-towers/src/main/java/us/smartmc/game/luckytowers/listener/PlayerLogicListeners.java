package us.smartmc.game.luckytowers.listener;

import me.imsergioh.pluginsapi.event.PlayerDataLoadedEvent;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import us.smartmc.game.luckytowers.LuckyTowers;
import us.smartmc.game.luckytowers.config.MainPluginConfig;
import us.smartmc.game.luckytowers.event.GameStatusChangeEvent;
import us.smartmc.game.luckytowers.event.player.GamePlayerDeathEvent;
import us.smartmc.game.luckytowers.event.player.GamePlayerJoinSessionEvent;
import us.smartmc.game.luckytowers.event.player.PlayerStatusChangeEvent;
import us.smartmc.game.luckytowers.instance.game.GameSession;
import us.smartmc.game.luckytowers.instance.game.GameSessionStatus;
import us.smartmc.game.luckytowers.instance.player.GamePlayer;
import us.smartmc.game.luckytowers.instance.player.PlayerStatus;
import us.smartmc.game.luckytowers.manager.PlayersManager;
import us.smartmc.game.luckytowers.menu.hotbar.SpectatorHotbar;
import us.smartmc.game.luckytowers.menu.hotbar.WaitingHotbar;
import us.smartmc.game.luckytowers.util.GameUtil;

public class PlayerLogicListeners implements Listener {

    @EventHandler
    public void createGamePlayer(PlayerDataLoadedEvent event) {
        GamePlayer.create(event.getPlayer().getUniqueId());
    }

    @EventHandler
    public void unloadGamePlayer(PlayerQuitEvent event) {
        PlayersManager manager = LuckyTowers.getManager(PlayersManager.class);
        manager.unregister(event.getPlayer().getUniqueId());
        System.out.println("Unloaded player " + event.getPlayer().getName());
    }

    @EventHandler
    public void addDeath(GamePlayerDeathEvent event) {
        event.getGamePlayer().addDeath();
    }

    @EventHandler
    public void addGamePlayed(PlayerStatusChangeEvent event) {
        if (event.getStatus().equals(PlayerStatus.INGAME)) event.getGamePlayer().addGamePlayed();
    }

    @EventHandler
    public void teleportToLobby(PlayerStatusChangeEvent event) {
        if (event.isCancelled()) return;
        if (!(event.getStatus().equals(PlayerStatus.LOBBY) && MainPluginConfig.isLobbyEnabled())) return;

        // Teleport to lobby if gameplayer is online (Should xd)
        event.getGamePlayer().onlinePlayer(player -> player.teleport(MainPluginConfig.getLobby()));
    }

    @EventHandler
    public void updateScoreboard(PlayerStatusChangeEvent event) {
        if (event.getPlayer() == null) return;
        if (!event.getPlayer().isOnline()) return;
        GameUtil.updateScoreboard(event.getGamePlayer());
    }

    @EventHandler
    public void clearInventory(GameStatusChangeEvent event) {
        GameSession session = event.getSession();
        if (session == null) return;
        if (!(session.getStatus().equals(GameSessionStatus.STARTING))) return;
        session.forEachOnlinePlayer(p -> p.getInventory().clear());
        session.forEachOnlinePlayer(p -> p.getInventory().setArmorContents(null));
    }

    @EventHandler
    public void giveWaitingHotbar(GamePlayerJoinSessionEvent event) {
        new WaitingHotbar(event.getPlayer()).set(event.getPlayer());
    }

    @EventHandler
    public void giveSpectatorHotbar(PlayerStatusChangeEvent event) {
        if (!(event.getStatus().equals(PlayerStatus.SPECTATING))) return;
        Bukkit.getScheduler().runTask(LuckyTowers.getPlugin(), () -> {
            if (!event.getPlayer().isOnline()) return;
            new SpectatorHotbar(event.getPlayer()).set(event.getPlayer());
        });
    }
}
