package us.smartmc.game.luckytowers.menu.hotbar;

import org.bukkit.entity.Player;
import us.smartmc.game.luckytowers.menu.GameMenu;
import us.smartmc.game.luckytowers.messages.GameItems;

public class WaitingHotbar extends GameMenu {

    public WaitingHotbar(Player player) {
        super(player, 9, "waiting");
    }

    @Override
    public void load() {
        set(0, item(GameItems.waiting_vote).get(), "playerOption vote");

        setLeaveItem(8);
    }

    @Override
    public void set(Player player) {
        initPlayer.getInventory().clear();
        super.set(player);
    }
}
