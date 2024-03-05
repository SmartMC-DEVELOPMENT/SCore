package us.smartmc.event.eventscore.itemcommand;

import me.imsergioh.pluginsapi.instance.ItemActionExecutor;
import me.imsergioh.pluginsapi.instance.item.ClickHandler;
import org.bukkit.entity.Player;

public class OpenEventControlCommand implements ItemActionExecutor {

    @Override
    public void execute(ClickHandler handler, String s, String[] args) {
        Player clicker = handler.clicker();
        String controlName = args[0];

        switch (controlName) {
            case "players":

                break;
        }
    }
}
