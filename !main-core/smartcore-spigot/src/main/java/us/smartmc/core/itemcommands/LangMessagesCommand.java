package us.smartmc.core.itemcommands;

import me.imsergioh.pluginsapi.instance.ItemActionExecutor;
import me.imsergioh.pluginsapi.instance.item.ClickHandler;
import us.smartmc.core.menu.langmessages.LangMessagesAdminSettingsMenu;

public class LangMessagesCommand implements ItemActionExecutor {

    @Override
    public void execute(ClickHandler clickHandler, String label, String[] args) {
        String cmdArg = args[0];
        if (cmdArg.equals("openSettings")) {
            new LangMessagesAdminSettingsMenu(clickHandler.player(), args[1]).open(clickHandler.player());
        }
    }
}