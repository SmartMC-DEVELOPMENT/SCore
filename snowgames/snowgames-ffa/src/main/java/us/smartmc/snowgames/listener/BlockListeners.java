package us.smartmc.snowgames.listener;

import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.server.ServerEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import us.smartmc.core.SmartCore;
import us.smartmc.snowgames.FFAPlugin;
import us.smartmc.snowgames.config.DefaultConfig;
import us.smartmc.snowgames.manager.BlocksResetManager;
import us.smartmc.snowgames.manager.ItemCooldownManager;

public class BlockListeners implements Listener {

    @EventHandler
    public void handleBlockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        if (SmartCore.getPlugin().getAdminModeHandler().isActive(player)) return;
        BlocksResetManager.registerBlockPlace(event.getBlockReplacedState());
        if (event.getBlock().getType().name().contains("PLATE")) {
            ItemCooldownManager cooldownManager = ItemCooldownManager.from(player);
            if (cooldownManager.hasActiveAtSlot(4)) {
                event.setCancelled(true);
                return;
            }
            ItemCooldownManager.from(player)
                    .registerAt(player.getInventory().getHeldItemSlot(),
                            DefaultConfig.getCooldown("propeller"));
        }
        if (event.getBlock().getType().name().contains("PLATE")) return;
        ItemStack item = player.getItemInHand();
        player.setItemInHand(item);
    }

    @EventHandler
    public void onWalkAtPlate(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (event.getAction().equals(Action.PHYSICAL)) {
            Block block = event.getClickedBlock();
            if (block == null) return;
            if (!block.getType().name().contains("PLATE")) return;

            Bukkit.getScheduler().runTaskLater(FFAPlugin.getPlugin(), () -> {
                Vector direction = player.getLocation().getDirection().normalize();
                direction.setY(direction.getY() + 0.55);
                direction.multiply(2.5);
                player.setVelocity(direction);
            }, 0);
        }
    }
}
