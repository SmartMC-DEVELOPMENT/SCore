package us.smartmc.core.itemcommands;

import me.imsergioh.pluginsapi.instance.ItemActionExecutor;
import me.imsergioh.pluginsapi.instance.item.ClickHandler;
import me.imsergioh.pluginsapi.instance.menu.CoreMenu;
import me.imsergioh.pluginsapi.instance.player.CorePlayer;
import us.smartmc.core.menu.langmessages.AdminLanguageMainMenu;

public class AdminLanguageCommand implements ItemActionExecutor {

    @Override
    public void execute(ClickHandler handler, String s, String[] args) {

        String cmdArg = args[0];

        switch (cmdArg) {
            case "toggleLanguage" -> {
                CoreMenu menu = CorePlayer.get(handler.getClicker()).getCurrentMenuOpen();
                if (menu == null) return;
                if (!(menu instanceof AdminLanguageMainMenu languageMainMenu)) return;
                languageMainMenu.toggleSelectedLanguage();
            }
        }


    }
}
