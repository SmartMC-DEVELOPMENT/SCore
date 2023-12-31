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

    private static final FFAPlugin plugin = FFAPlugin.getPlugin();

    private final List<GameMap> maps;
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

        GameMap currentMap = getCurrentMap();
        if (!(currentMap instanceof FFAMap)) return;
        currentMap = FFAMap.get(getCurrentMap().getName());

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

    public GameMap getCurrentMap() {
        if (maps.isEmpty()) return null;
        return maps.get(currentIndex);
    }
}

