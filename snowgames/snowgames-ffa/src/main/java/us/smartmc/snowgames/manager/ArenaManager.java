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

public class ArenaManager {

    private static final FFAPlugin plugin = FFAPlugin.getPlugin();

    private final List<String> mapsName;
    private int currentIndex;

    public ArenaManager() {
        this.mapsName = new ArrayList<>();
        this.currentIndex = 0;
        GameMapManager.values().forEach(gameMap -> {
            if (gameMap instanceof FFAMap ffaMap) {
                mapsName.add(ffaMap.getName());
            }
        });
    }

    public void rotateMap() {
        FFAMap currentMap = getCurrentMap();
        // Update currentIndex
        if (!mapsName.isEmpty()) {
            currentIndex = (currentIndex + 1) % mapsName.size();
        }

        GameMap nextMap = getNextMap();
        if (nextMap == null) {
            System.out.println("Can't rotate Map at ArenaManager because 'nextMap' is null!");
            return;
        }

        while (nextMap.equals(currentMap) && mapsName.size() > 1) {
            nextMap = getNextMap();
        }

        if (!(nextMap instanceof FFAMap ffaMap)) return;
        ffaMap.loadWorld();
        FFAPlugin.getGame().setMap(ffaMap);

        // Same Map (Only 1 FFA-Map)
        if (currentMap.equals(ffaMap)) {
            ffaMap.setTimeAlive(ffaMap.getMaxArenaTime());
            ffaMap.startChangeMapTask();
        }

        new BukkitRunnable() {
            @Override
            public void run() {
                for (Player player : Bukkit.getServer().getOnlinePlayers()) {
                    player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 20 * 2, 1));
                    player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 20 * 2, 1));
                    player.teleport(ffaMap.getSpawn());
                }
                currentMap.unloadWorld();
            }
        }.runTaskLater(plugin, 10);
    }

    public GameMap getNextMap() {
        if (mapsName.isEmpty()) return null;
        String name = mapsName.get(currentIndex);
        return GameMapManager.get(name);
    }

    public FFAMap getCurrentMap() {
        return FFAPlugin.getGame().getMap();
    }
}

