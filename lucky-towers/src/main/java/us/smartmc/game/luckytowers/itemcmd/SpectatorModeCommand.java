package us.smartmc.game.luckytowers.itemcmd;

import me.imsergioh.pluginsapi.instance.ItemActionExecutor;
import me.imsergioh.pluginsapi.instance.item.ClickHandler;
import org.bukkit.entity.Player;
import us.smartmc.game.luckytowers.menu.PlayerNavigatorMenu;

import java.util.UUID;

public class SpectatorModeCommand implements ItemActionExecutor {

    @Override
    public void execute(ClickHandler clickHandler, String label, String[] args) {
        Player player = clickHandler.player();

        if (args[0].equals("navigator")) {
            new PlayerNavigatorMenu(player).open(player);
        }
    }
}
