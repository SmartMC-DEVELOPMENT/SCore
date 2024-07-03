package us.smartmc.game.listener;

import me.imsergioh.pluginsapi.region.BukkitCuboidRegion;
import me.imsergioh.pluginsapi.util.PaperChatUtil;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import us.smartmc.game.SkyBlockPlugin;
import us.smartmc.game.instance.DefaultIsland;
import us.smartmc.game.manager.IslandsSchematicsManager;
import us.smartmc.skyblock.manager.IslandsManager;

import java.util.Objects;

public class TestListeners implements Listener {

    @EventHandler
    public void generateDefault(AsyncPlayerChatEvent event){
        if (!event.getPlayer().getName().equals("ImSergioh")) return;
        if (!event.getMessage().equals("generate default-island")) return;
        DefaultIsland island = (DefaultIsland) IslandsManager.get(IslandsSchematicsManager.getDefaultIslandId());
        Bukkit.getScheduler().runTask(SkyBlockPlugin.getPlugin(), () -> {
            island.setupIsland();
            World world = island.getWorld();
            event.getPlayer().teleport(world.getSpawnLocation());
        });
    }

    @EventHandler
    public void saveDefault(AsyncPlayerChatEvent event){
        if (!event.getPlayer().getName().equals("ImSergioh")) return;
        if (!event.getMessage().equals("save default-island")) return;

        DefaultIsland island = (DefaultIsland) IslandsManager.get(IslandsSchematicsManager.getDefaultIslandId());

        World world = island.getWorld();
        try {
            IslandsSchematicsManager.saveRegion(world, island);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        Bukkit.getScheduler().runTask(SkyBlockPlugin.getPlugin(), () -> {
            island.setupIsland();

            event.getPlayer().teleport(world.getSpawnLocation());
        });
    }

}
