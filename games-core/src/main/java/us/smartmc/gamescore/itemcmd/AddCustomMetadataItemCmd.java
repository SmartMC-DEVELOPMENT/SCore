package us.smartmc.gamescore.itemcmd;

import me.imsergioh.pluginsapi.instance.ItemActionExecutor;
import me.imsergioh.pluginsapi.instance.item.ClickHandler;
import me.imsergioh.pluginsapi.instance.item.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;

public class AddCustomMetadataItemCmd implements ItemActionExecutor {

    @Override
    public void execute(ClickHandler handler, String label, String[] args) {
        Player player = handler.getPlayer();
        String regionName = args[0];

        Inventory inventory = Bukkit.createInventory(null, InventoryType.ANVIL);
        inventory.setItem(1, ItemBuilder.of(Material.NAME_TAG).name("Introduce nombre").get());
        player.closeInventory();
        player.openInventory(inventory);
    }
}
