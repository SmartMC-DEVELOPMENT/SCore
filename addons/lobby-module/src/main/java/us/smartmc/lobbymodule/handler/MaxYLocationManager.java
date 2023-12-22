package us.smartmc.lobbymodule.handler;

import us.smartmc.core.SmartCore;
import us.smartmc.core.handler.SpawnHandler;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import us.smartmc.lobbymodule.LobbyModule;

public class MaxYLocationManager {

    private static int taskID;

    public static void start() {
        if (!LobbyModule.getLobbyConfig().isTeleportAtMaxY()) return;
        taskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(SmartCore.getPlugin(), () -> {
            int maxY = LobbyModule.getLobbyConfig().get("max_y_location", Number.class).intValue();
            for (Player player : Bukkit.getOnlinePlayers()) {
                World world = player.getLocation().getWorld();
                if (!world.equals(SpawnHandler.getLocation().getWorld())) continue;
                int yPlayerLoc = player.getLocation().getBlockY();
                if (maxY > yPlayerLoc) {
                    player.chat("/spawn");
                }
            }
        }, 10, 10);
    }

    public static void cancelTask() {
        Bukkit.getScheduler().cancelTask(taskID);
    }

}
