package us.smartmc.game.luckytowers.manager;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import us.smartmc.game.luckytowers.LuckyTowers;
import us.smartmc.game.luckytowers.instance.player.GamePlayer;
import us.smartmc.game.luckytowers.instance.player.PlayerStatus;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class VanishManager {

    private static final LuckyTowers plugin = LuckyTowers.getPlugin();

    private static BukkitRunnable runnable;

    public static void startTask() {
        runnable = new BukkitRunnable() {
            @Override
            public void run() {

                Set<GamePlayer> inGamePlayers = getStatusPlayers(PlayerStatus.INGAME);
                Set<GamePlayer> spectators = getStatusPlayers(PlayerStatus.SPECTATING);

                inGamePlayers.forEach(inGamePlayer -> {
                    spectators.forEach(spectator -> {
                        inGamePlayer.getBukkitPlayer().hidePlayer(plugin, spectator.getBukkitPlayer());
                        spectator.getBukkitPlayer().showPlayer(plugin, inGamePlayer.getBukkitPlayer());
                    });
                });

                getStatusPlayers(PlayerStatus.LOBBY).forEach(lobbyPlayer -> {
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        lobbyPlayer.getBukkitPlayer().showPlayer(plugin, player);
                    }
                });

            }
        };
        runnable.runTaskTimerAsynchronously(LuckyTowers.getPlugin(), 1, 1);
    }

    public static Set<GamePlayer> getStatusPlayers(PlayerStatus... statuses) {
        Set<GamePlayer> list = new HashSet<>();
        for (Player player : Bukkit.getOnlinePlayers()) {
            GamePlayer gamePlayer = GamePlayer.get(player.getUniqueId());
            if (gamePlayer == null) continue;
            if (Arrays.stream(statuses).collect(Collectors.toSet()).contains(gamePlayer.getStatus())) {
                list.add(gamePlayer);
            }
        }
        return list;
    }

}
