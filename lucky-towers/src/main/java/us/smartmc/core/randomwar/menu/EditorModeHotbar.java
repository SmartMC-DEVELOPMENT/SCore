package us.smartmc.core.randomwar.menu;

import me.imsergioh.pluginsapi.instance.item.ItemBuilder;
import me.imsergioh.pluginsapi.instance.menu.CoreMenu;
import me.imsergioh.pluginsapi.instance.player.CorePlayer;
import me.imsergioh.pluginsapi.language.IMessageCategory;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import us.smartmc.core.randomwar.messages.GameMessages;

public class EditorModeHotbar extends CoreMenu {

    private ItemStack[] oldInventory;

    public EditorModeHotbar(Player player) {
        super(player, InventoryType.PLAYER.getDefaultSize(), "editor");
        oldInventory = player.getInventory().getContents();
    }

    @Override
    public void load() {
        set(0, item(GameMessages.editorMode_item_selectorTeam).get());
    }

    private ItemBuilder item(IMessageCategory category) {
        return ItemBuilder.of(initCorePlayer.getLanguage(), category);
    }

    public void restore(Player player) {
        CorePlayer corePlayer = CorePlayer.get(player);
        if (!(corePlayer.getCurrentMenuSet() instanceof EditorModeHotbar menu)) return;
        player.getInventory().clear();
        player.getInventory().setContents(menu.oldInventory);
        corePlayer.setCurrentMenuSet(null);
    }

}
