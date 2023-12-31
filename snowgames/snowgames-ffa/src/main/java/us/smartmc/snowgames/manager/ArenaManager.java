package us.smartmc.snowgames.manager;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import us.smartmc.gamesmanager.game.map.GameMap;
import us.smartmc.snowgames.FFAPlugin;
import us.smartmc.snowgames.game.FFAMap;

import java.util.ArrayList;
import java.util.List;

import static us.smartmc.gamesmanager.manager.GameMapManager.values;

public class ArenaManager {

    private static FFAPlugin plugin = FFAPlugin.getPlugin();

    private List<GameMap> maps;
    private int currentIndex;

    public ArenaManager() {
        this.maps = new ArrayList<>();
        this.currentIndex = 0;

        maps.addAll(values());
    }

    public void rotateMap() {
        if (!maps.isEmpty()) {
            currentIndex = (currentIndex + 1) % maps.size();
        }

        FFAMap currentMap = (FFAMap) FFAMap.get(getCurrentMap().getName());
        if (currentMap == null) return;
        broadcastCountdownMessages(currentMap.getName(), 30, 15, 5, 4, 3, 2, 1);

        new BukkitRunnable() {
            @Override
            public void run() {
                for (Player player : Bukkit.getServer().getOnlinePlayers()) {
                    player.teleport(currentMap.getSpawn());
                    player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 3, 100));
                    player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 3, 100));
                }
            }
        }.runTaskLater(plugin, 30 * 20);
    }

    private void broadcastCountdownMessages(String mapName, int... countdowns) {
        for (int countdown : countdowns) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    Bukkit.broadcastMessage("¡Cambiando a mapa " + mapName + " en " + countdown + " segundos!");
                }
            }.runTaskLater(plugin, (30 - countdown) * 20L);
        }
    }

    public GameMap getCurrentMap() {
        if (maps.isEmpty()) return null;
        return maps.get(currentIndex);
    }
}

