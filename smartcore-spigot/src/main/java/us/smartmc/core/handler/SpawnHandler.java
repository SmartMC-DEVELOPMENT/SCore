package us.smartmc.core.handler;

import me.imsergioh.pluginsapi.instance.FilePluginConfig;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import us.smartmc.core.SmartCore;
import us.smartmc.core.util.ConfigUtils;

import java.io.File;

public class SpawnHandler implements Listener {

    private static final JavaPlugin plugin = SmartCore.getPlugin();

    private static FilePluginConfig config;
    private static Location location;

    public static void setup() {
        config = new FilePluginConfig(new File(plugin.getDataFolder(), "spawn.json"));
        config.load();
        config.registerDefault("enabled", true);
        getLocation();
    }

    public static void set(Location l) {
        location = l;
        ConfigUtils.saveLocation(config, "spawn", l);
    }

    public static Location getLocation() {
        if (location == null && config.containsKey("spawn")) {
            Location loc = ConfigUtils.loadLocation(config, "spawn");
            location = loc;
            return loc;
        }
        return location;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        if (!config.getBoolean("enabled")) return;
        if (getLocation() == null) return;
        event.getPlayer().teleport(getLocation());
    }
}
