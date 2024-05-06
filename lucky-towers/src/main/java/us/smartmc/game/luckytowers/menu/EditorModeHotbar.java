package us.smartmc.game.luckytowers.menu;

import me.imsergioh.pluginsapi.instance.player.CorePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import us.smartmc.game.luckytowers.messages.GameMessages;

public class EditorModeHotbar extends GameMenu {

    private final ItemStack[] oldInventory;

    public EditorModeHotbar(Player player) {
        super(player, 36, "editor");
        oldInventory = player.getInventory().getContents();
    }

    @Override
    public void load() {
        set(0, item(GameMessages.editorMode_item_selectorTeam).get(), "adminEditor selectTeam");
        set(1, item(GameMessages.editorMode_item_addSpawn).get(), "adminEditor addTeamSpawn");
    }

    public void restore(Player player) {
        CorePlayer corePlayer = CorePlayer.get(player);
        if (!(corePlayer.getCurrentMenuSet() instanceof EditorModeHotbar menu)) return;
        player.getInventory().clear();
        player.getInventory().setContents(menu.oldInventory);
        corePlayer.setCurrentMenuSet(null);
    }

}
