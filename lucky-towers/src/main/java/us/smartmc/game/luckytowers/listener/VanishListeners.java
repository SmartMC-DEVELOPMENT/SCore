package us.smartmc.game.luckytowers.listener;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;
import us.smartmc.game.luckytowers.LuckyTowers;
import us.smartmc.game.luckytowers.event.player.PlayerStatusChangeEvent;
import us.smartmc.game.luckytowers.instance.game.GameSession;
import us.smartmc.game.luckytowers.instance.player.GamePlayer;
import us.smartmc.game.luckytowers.instance.player.PlayerStatus;

public class VanishListeners implements Listener {

    private static final LuckyTowers plugin = LuckyTowers.getPlugin();

    @EventHandler
    public void statusChange(PlayerStatusChangeEvent event) {
        PlayerStatus status = event.getStatus();
        GamePlayer gamePlayer = event.getGamePlayer();
        GameSession session = gamePlayer.getGameSession();

        if (status.equals(PlayerStatus.INGAME)) {
            Bukkit.getOnlinePlayers().forEach(onlinePlayer -> {
                event.getPlayer().hidePlayer(plugin, onlinePlayer);
            });
            if (session != null)
                session.getAlivePlayers().forEach(alivePlayer -> {
                    event.getPlayer().showPlayer(plugin, alivePlayer.getBukkitPlayer());
                });
        }

        if (status.equals(PlayerStatus.SPECTATING)) {
            Bukkit.getOnlinePlayers().forEach(onlinePlayer -> {
                event.getPlayer().hidePlayer(plugin, onlinePlayer);
            });
            if (session != null)
                session.getPlayers().forEach(alivePlayer -> {
                    event.getPlayer().showPlayer(plugin, alivePlayer.getBukkitPlayer());
                });
        }

        if (status.equals(PlayerStatus.LOBBY)) {
            Bukkit.getOnlinePlayers().forEach(onlinePlayer -> {
                GamePlayer onlineGamePlayer = GamePlayer.get(onlinePlayer.getUniqueId());
                if (onlineGamePlayer == null) return;
                if (onlineGamePlayer.getStatus().equals(PlayerStatus.LOBBY)) {
                    onlinePlayer.showPlayer(plugin, event.getPlayer());
                    event.getPlayer().showPlayer(plugin, onlinePlayer);
                } else {
                    onlinePlayer.hidePlayer(plugin, event.getPlayer());
                    event.getPlayer().hidePlayer(plugin, onlinePlayer);
                }
            });
        }

    }
}
