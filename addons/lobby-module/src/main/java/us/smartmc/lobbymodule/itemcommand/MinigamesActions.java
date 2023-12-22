package us.smartmc.lobbymodule.itemcommand;

import org.bukkit.entity.Player;
import us.smartmc.core.instance.player.PlayerServerConnect;
import us.smartmc.core.pluginsapi.spigot.instance.ItemActionExecutor;
import us.smartmc.core.pluginsapi.spigot.item.ClickHandler;
import us.smartmc.lobbymodule.config.MinigamesConfig;

public class MinigamesActions implements ItemActionExecutor {

    @Override
    public void execute(ClickHandler clickHandler, String s, String[] strings) {
        Player player = clickHandler.clicker();
        String cmdLabel = MinigamesConfig.getLabelCommand(s);
        if (cmdLabel.startsWith("server")) new PlayerServerConnect(player,
                cmdLabel.split(" ")[1]);
    }
}
