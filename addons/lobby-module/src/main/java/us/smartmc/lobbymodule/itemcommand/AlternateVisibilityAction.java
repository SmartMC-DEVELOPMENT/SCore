package us.smartmc.lobbymodule.itemcommand;

import me.imsergioh.pluginsapi.instance.ItemActionExecutor;
import me.imsergioh.pluginsapi.instance.item.ClickHandler;
import me.imsergioh.pluginsapi.instance.player.CorePlayer;
import us.smartmc.lobbymodule.menu.SettingsMenu;

public class AlternateVisibilityAction implements ItemActionExecutor {

    @Override
    public void execute(ClickHandler clickHandler, String label, String[] args) {
        clickHandler.clicker().chat("/changeVisibility");
        try {
            SettingsMenu settingsMenu = (SettingsMenu) CorePlayer.get(clickHandler.getPlayer()).getCurrentMenuOpen();
            settingsMenu.setVisibilityItem();
            clickHandler.clicker().getOpenInventory().setItem(15, settingsMenu.get(15));
        } catch (Exception ignore) {}
    }
}
