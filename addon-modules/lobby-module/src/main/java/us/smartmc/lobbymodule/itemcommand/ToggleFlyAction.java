package us.smartmc.lobbymodule.itemcommand;

import me.imsergioh.pluginsapi.instance.ItemActionExecutor;
import me.imsergioh.pluginsapi.instance.item.ClickHandler;
import me.imsergioh.pluginsapi.instance.menu.GUIMenu;
import me.imsergioh.pluginsapi.instance.player.CorePlayer;
import us.smartmc.lobbymodule.menu.SettingsMenu;

public class ToggleFlyAction implements ItemActionExecutor {

    @Override
    public void execute(ClickHandler clickHandler, String label, String[] args) {
        clickHandler.clicker().chat("/fly");
        try {
            SettingsMenu settingsMenu = (SettingsMenu) GUIMenu.getOpenGUI(clickHandler.player());
            settingsMenu.setFlyingItem();
            clickHandler.clicker().getOpenInventory().setItem(15, settingsMenu.get(15));
        } catch (Exception ignore) {}
    }
}
