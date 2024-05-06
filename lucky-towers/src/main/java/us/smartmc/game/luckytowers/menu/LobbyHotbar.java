package us.smartmc.game.luckytowers.menu;

import org.bukkit.entity.Player;
import us.smartmc.game.luckytowers.messages.GameMessages;

public class LobbyHotbar extends GameMenu {

    public LobbyHotbar(Player player) {
        super(player, 36, "lobbyHotbar");
    }

    @Override
    public void load() {
        set(0, item(GameMessages.lobbyHotbar_item_play).get(), "lobbyHotbar play");
        set(4, item(GameMessages.lobbyHotbar_item_options).get(), "lobbyHotbar options");
    }

}
