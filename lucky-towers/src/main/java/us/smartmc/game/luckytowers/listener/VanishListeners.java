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
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (session.getPlayers().contains(GamePlayer.get(player.getUniqueId()))) continue;
                event.getPlayer().hidePlayer(plugin, player);
            }
        }

        if (status.equals(PlayerStatus.SPECTATING)) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (session.getPlayers().contains(GamePlayer.get(player.getUniqueId()))) continue;
                player.hidePlayer(plugin, event.getPlayer());
            }
        }

        if (status.equals(PlayerStatus.LOBBY)) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (!GamePlayer.get(player.getUniqueId()).getStatus().equals(PlayerStatus.LOBBY)) continue;
                player.showPlayer(plugin, event.getPlayer());
                event.getPlayer().showPlayer(plugin, player);
            }
        }

    }
}
