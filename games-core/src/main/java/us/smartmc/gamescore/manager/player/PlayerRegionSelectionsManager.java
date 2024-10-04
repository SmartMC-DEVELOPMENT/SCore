package us.smartmc.gamescore.manager.player;

import me.imsergioh.pluginsapi.util.ChatUtil;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import us.smartmc.gamescore.adminplayer.PlayerRegionSelectSession;
import us.smartmc.gamescore.instance.manager.MapManager;

import java.util.UUID;

public class PlayerRegionSelectionsManager extends MapManager<UUID, PlayerRegionSelectSession> {

    public static final ItemStack wandItem = new ItemStack(Material.DIAMOND_AXE);

    static {
        ItemMeta meta = wandItem.getItemMeta();
        meta.setDisplayName(ChatUtil.parse("&4&lWAND ITEM GAME-CORE"));
        wandItem.setItemMeta(meta);
    }

    public static boolean canWand(Player player) {
        ItemStack itemStack = player.getItemInHand();
        if (itemStack == null) return false;
        if (!player.hasPermission("*")) return false;
        return itemStack.getType().equals(wandItem.getType()) && itemStack.hasItemMeta() &&
                itemStack.getItemMeta().getDisplayName().equals(wandItem.getItemMeta().getDisplayName());
    }

    @Override
    public PlayerRegionSelectSession createValueByKey(UUID id) {
        return new PlayerRegionSelectSession(id);
    }
}
