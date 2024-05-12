package us.smartmc.game.luckytowers.menu;

import org.bukkit.entity.Player;
import us.smartmc.game.luckytowers.messages.GameItems;
import us.smartmc.game.luckytowers.messages.GameMessages;

public class VoteMenu extends GameMenu {

    public VoteMenu(Player player) {
        super(player, 9 * 3, GameMessages.menu_vote_title);
    }

    @Override
    public void load() {
        set(13, item(GameItems.upcoming_feature).get());
    }
}
