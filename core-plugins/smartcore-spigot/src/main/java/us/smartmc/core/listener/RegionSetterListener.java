package us.smartmc.core.listener;

import me.imsergioh.pluginsapi.util.ChatUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import us.smartmc.core.SmartCore;
import us.smartmc.core.regions.controller.CuboidEditorPlayerSession;
import us.smartmc.core.regions.controller.RegionModeManager;

import static us.smartmc.core.util.ItemUtils.wand;

public class RegionSetterListener implements Listener {

    private static final SmartCore plugin = SmartCore.getPlugin();
    private static final RegionModeManager regionManager = plugin.getRegionModeManager();

    @EventHandler
    public void setLoc2(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = player.getItemInHand();
        if (event.getClickedBlock() == null) return;
        if (!item.equals(wand)) return;
        if (!event.getAction().name().contains("RIGHT")) return;
        // Enable if session is null
        if (regionManager.get(player.getUniqueId()) == null) regionManager.toggleMode(player);

        CuboidEditorPlayerSession session = regionManager.get(player.getUniqueId());
        session.getEditorCuboid().setLoc2(event.getClickedBlock().getLocation());
        String message = ChatUtil.parse(player, "<lang.general.region_pos2>", event.getClickedBlock().getLocation().toString());
        player.sendMessage(message);
        event.setCancelled(true);
    }

    @EventHandler
    public void setLoc1(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = player.getItemInHand();
        if (event.getClickedBlock() == null) return;
        if (!item.equals(wand)) return;
        if (!event.getAction().name().contains("LEFT")) return;
        // Enable if session is null
        if (regionManager.get(player.getUniqueId()) == null) regionManager.toggleMode(player);

        CuboidEditorPlayerSession session = regionManager.get(player.getUniqueId());
        session.getEditorCuboid().setLoc1(event.getClickedBlock().getLocation());
        String message = ChatUtil.parse(player, "<lang.general.region_pos1>", event.getClickedBlock().getLocation().toString());
        player.sendMessage(message);
        event.setCancelled(true);
    }
}