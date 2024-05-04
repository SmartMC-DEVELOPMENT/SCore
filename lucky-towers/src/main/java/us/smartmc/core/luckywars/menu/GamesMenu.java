package us.smartmc.core.luckywars.menu;

import org.bukkit.entity.Player;
import us.smartmc.core.luckywars.messages.GameMessages;

public class GamesMenu extends GameMenu {

    public GamesMenu(Player player) {
        super(player, 54, GameMessages.menu_games_title);
    }

    @Override
    public void load() {

    }

}
