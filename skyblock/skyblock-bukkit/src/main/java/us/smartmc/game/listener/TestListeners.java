package us.smartmc.game.listener;

import me.imsergioh.pluginsapi.region.BukkitCuboidRegion;
import me.imsergioh.pluginsapi.util.PaperChatUtil;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class TestListeners implements Listener {

    private Location p1, p2;
    private BukkitCuboidRegion region;

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        if (!player.getName().equals("ImSergioh")) return;

        if (event.getMessage().contains("pos1")) {
            p1 = player.getLocation();
            if (!createRegion(player.getLocation().getWorld())) return;
            player.sendMessage(PaperChatUtil.parse("&aCREADA REGIÓN TEST!"));
        }

        if (event.getMessage().contains("pos2")) {
            p2 = player.getLocation();
            if (!createRegion(player.getLocation().getWorld())) return;
            player.sendMessage(PaperChatUtil.parse("&aCREADA REGIÓN TEST!"));
        }

        if (event.getMessage().contains("fill")) {
            region.getBukkitLocationsList().forEachRemaining(location -> {
                player.sendBlockChange(location, Material.DIAMOND_BLOCK.createBlockData());
            });
        }

    }

    private boolean createRegion(World world) {
        if (p1 != null && p2 != null) {
            region = new BukkitCuboidRegion(world.getName(), p1, p2);
            return true;
        }
        return false;
    }

}
