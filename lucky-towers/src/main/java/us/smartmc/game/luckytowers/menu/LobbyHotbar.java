package us.smartmc.game.luckytowers.menu;

import me.imsergioh.pluginsapi.instance.item.ItemBuilder;
import me.imsergioh.pluginsapi.instance.menu.CoreMenu;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;

public class LobbyHotbar extends CoreMenu {

    protected LobbyHotbar(Player player) {
        super(player, InventoryType.PLAYER.getDefaultSize(), "lobbyHotbar");
    }

    @Override
    public void load() {
        initPlayer.closeInventory();
        set(0, ItemBuilder.of(Material.DIAMOND_SWORD).get(), "lobbyHotbar play");

        set(0, ItemBuilder.of(Material.DIAMOND_SWORD).get(), "lobbyHotbar options");
    }

}
