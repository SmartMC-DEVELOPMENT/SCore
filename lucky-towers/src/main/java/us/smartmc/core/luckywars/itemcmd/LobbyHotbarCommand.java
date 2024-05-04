package us.smartmc.core.luckywars.itemcmd;

import me.imsergioh.pluginsapi.instance.ItemActionExecutor;
import me.imsergioh.pluginsapi.instance.item.ClickHandler;
import org.bukkit.entity.Player;
import us.smartmc.core.luckywars.menu.GamesMenu;

public class LobbyHotbarCommand implements ItemActionExecutor {

    @Override
    public void execute(ClickHandler clickHandler, String label, String[] args) {
        Player player = clickHandler.player();

        if (args[0].equalsIgnoreCase("play")) {
            new GamesMenu(player).open(player);
        }
    }
}
