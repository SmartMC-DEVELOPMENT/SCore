package us.smartmc.game.luckytowers.menu.hotbar;

import org.bukkit.entity.Player;
import us.smartmc.game.luckytowers.menu.GameMenu;
import us.smartmc.game.luckytowers.messages.GameItems;

public class SpectatorHotbar extends GameMenu {

    public SpectatorHotbar(Player player) {
        super(player, 9, "spectator");
    }

    @Override
    public void load() {
        set(0, item(GameItems.spectator_compass).get(), "spectatorMode navigator");
        setLeaveItem(8);
    }

    @Override
    public void set(Player player) {
        initPlayer.getInventory().clear();
        super.set(player);
    }
}
