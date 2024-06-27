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
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Objects;

public class TestListeners implements Listener {

    private Location p1, p2;
    private BukkitCuboidRegion region;

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        if (!player.getName().equals("ImSergioh")) return;

        if (event.getMessage().contains("fill")) {
            region.getBukkitLocationsList().forEachRemaining(location -> {
                player.sendBlockChange(location, Material.DIAMOND_BLOCK.createBlockData());
            });
            player.sendBlockChange(region.getBukkitPos1(), Material.REDSTONE_BLOCK.createBlockData());
            player.sendBlockChange(region.getBukkitPos2(), Material.EMERALD_BLOCK.createBlockData());
        }
    }

    @EventHandler
    public void interactPositions(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (!player.getName().equals("ImSergioh")) return;
        ItemStack item = player.getInventory().getItemInMainHand();
        if (!item.getType().equals(Material.DIAMOND_AXE)) return;

        if (!event.getAction().name().contains("BLOCK")) return;
        boolean isPosition1 = event.getAction().name().contains("LEFT");

        if (isPosition1) {
            p1 = Objects.requireNonNull(event.getClickedBlock()).getLocation();
            player.sendMessage(PaperChatUtil.parse("&aPOSITION 1 SET"));
            event.setCancelled(true);

        } else {
            p2 = Objects.requireNonNull(event.getClickedBlock()).getLocation();
            player.sendMessage(PaperChatUtil.parse("&aPOSITION 2 SET"));
            event.setCancelled(true);
        }

        if (!createRegion(player.getLocation().getWorld())) return;
        player.sendMessage(PaperChatUtil.parse("&aCREADA REGIÓN TEST!"));
    }

    private boolean createRegion(World world) {
        if (p1 != null && p2 != null) {
            region = new BukkitCuboidRegion(world.getName(), p1, p2);
            return true;
        }
        return false;
    }

}
