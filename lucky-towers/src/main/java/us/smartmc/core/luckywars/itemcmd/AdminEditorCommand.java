package us.smartmc.core.luckywars.itemcmd;

import me.imsergioh.pluginsapi.instance.ItemActionExecutor;
import me.imsergioh.pluginsapi.instance.item.ClickHandler;
import org.bukkit.entity.Player;

public class AdminEditorCommand implements ItemActionExecutor {

    @Override
    public void execute(ClickHandler clickHandler, String label, String[] args) {
        Player player = clickHandler.player();

    }
}
