package us.smartmc.lobbymodule.itemcommand;

import me.imsergioh.pluginsapi.instance.ItemActionExecutor;
import me.imsergioh.pluginsapi.instance.item.ClickHandler;
import us.smartmc.lobbymodule.menu.LobbiesMenu;
import us.smartmc.lobbymodule.menu.MinigamesMenu;
import us.smartmc.lobbymodule.menu.SettingsMenu;

public class LobbyModuleAction implements ItemActionExecutor {

    @Override
    public void execute(ClickHandler clickHandler, String label, String[] args) {
        try {
            String arg = args[0].toLowerCase();

            if (arg.equals("settings")) {
                new SettingsMenu(clickHandler.player()).open(clickHandler.player());
            }

            if (arg.equals("lobbies")) {
                new LobbiesMenu(clickHandler.player()).open(clickHandler.player());
            }

            if (arg.equals("minigames")) {
                new MinigamesMenu(clickHandler.player()).open(clickHandler.player());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
