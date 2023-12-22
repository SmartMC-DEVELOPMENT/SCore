package us.smartmc.lobbymodule.itemcommand;

import us.smartmc.core.pluginsapi.spigot.instance.ItemActionExecutor;
import us.smartmc.core.pluginsapi.spigot.item.ClickHandler;
import us.smartmc.core.pluginsapi.spigot.menu.CoreMenu;
import us.smartmc.core.pluginsapi.spigot.player.CorePlayer;
import us.smartmc.lobbymodule.menu.SettingsMenu;

public class ToggleFlyAction implements ItemActionExecutor {

    @Override
    public void execute(ClickHandler clickHandler, String label, String[] args) {
        clickHandler.clicker().chat("/fly");
        try {
            SettingsMenu settingsMenu = (SettingsMenu) CorePlayer.get(clickHandler.getPlayer()).getCurrentMenuOpen();
            settingsMenu.setFlyingItem();
            clickHandler.clicker().getOpenInventory().setItem(15, settingsMenu.get(15));
        } catch (Exception ignore) {}
    }
}
