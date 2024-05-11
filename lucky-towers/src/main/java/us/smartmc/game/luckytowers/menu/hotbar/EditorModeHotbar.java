package us.smartmc.game.luckytowers.menu.hotbar;

import me.imsergioh.pluginsapi.instance.menu.CoreMenu;
import me.imsergioh.pluginsapi.instance.player.CorePlayer;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import us.smartmc.game.luckytowers.menu.GameMenu;
import us.smartmc.game.luckytowers.messages.AdminItems;

public class EditorModeHotbar extends GameMenu {

    private final CoreMenu oldMenu;

    public EditorModeHotbar(Player player) {
        super(player, 36, "editor");
        oldMenu = CorePlayer.get(player).getCurrentMenuSet();
    }

    @Override
    public void load() {
        set(0, item(AdminItems.editorMode_item_setSpawn).get(), "adminEditor setSpawn");
        set(1, item(AdminItems.editorMode_item_addSpawn).get(), "adminEditor addTeamSpawn");

        set(4, item(AdminItems.editorMode_item_removeLastSpawn).get(), "adminEditor removeLastSpawn");

        set(7, item(AdminItems.editorMode_item_toggleMaintenance).get(), "adminEditor toggleMaintenance");
        set(8, item(AdminItems.editorMode_item_setCorners).get(), "adminEditor setCorner");
    }

    public void restore(Player player) {
        player.getInventory().clear();
        oldMenu.set(player);
    }

}
