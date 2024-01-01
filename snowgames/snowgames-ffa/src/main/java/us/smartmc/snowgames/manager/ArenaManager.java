package us.smartmc.snowgames.manager;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import us.smartmc.gamesmanager.game.map.GameMap;
import us.smartmc.gamesmanager.manager.GameMapManager;
import us.smartmc.snowgames.FFAPlugin;
import us.smartmc.snowgames.game.FFAMap;

import java.util.ArrayList;
import java.util.List;

import static us.smartmc.gamesmanager.manager.GameMapManager.values;

public class ArenaManager {

    private static final FFAPlugin plugin = FFAPlugin.getPlugin();

    private final List<String> mapsName;
    private int currentIndex;

    public ArenaManager() {
        this.mapsName = new ArrayList<>();
        this.currentIndex = 0;

        values().forEach(gameMap -> {
            if (gameMap instanceof FFAMap ffaMap) {
                mapsName.add(ffaMap.getName());
            }
        });
    }

    public void rotateMap() {
        if (!mapsName.isEmpty()) {
            currentIndex = (currentIndex + 1) % mapsName.size();
        }

        GameMap currentMap = getCurrentMap();
        if (!(currentMap instanceof FFAMap ffaMap)) return;
        FFAPlugin.getGame().setMap(ffaMap);

        new BukkitRunnable() {
            @Override
            public void run() {
                for (Player player : Bukkit.getServer().getOnlinePlayers()) {
                    player.teleport(ffaMap.getSpawn());
                    player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 3, 100));
                    player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 3, 100));
                }
            }
        }.runTaskLater(plugin, 30 * 20);
    }

    public GameMap getCurrentMap() {
        if (mapsName.isEmpty()) return null;
        String name = mapsName.get(currentIndex);
        return GameMapManager.get(name);
    }
}

