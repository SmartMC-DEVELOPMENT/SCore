package us.smartmc.game.luckytowers.menu;

import org.bukkit.entity.Player;
import us.smartmc.game.luckytowers.messages.GameMessages;

public class OptionsMenu extends GameMenu {

    protected OptionsMenu(Player player) {
        super(player, 27, GameMessages.menu_options_title);
    }

    @Override
    public void load() {

    }
}
