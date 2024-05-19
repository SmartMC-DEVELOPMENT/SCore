package us.smartmc.lobbymodule.itemcommand;

import me.imsergioh.pluginsapi.instance.ItemActionExecutor;
import me.imsergioh.pluginsapi.instance.PlayerLanguages;
import me.imsergioh.pluginsapi.instance.item.ClickHandler;
import me.imsergioh.pluginsapi.instance.player.CorePlayer;
import me.imsergioh.pluginsapi.language.Language;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import us.smartmc.lobbymodule.menu.LobbiesMenu;
import us.smartmc.lobbymodule.menu.MinigamesMenu;
import us.smartmc.lobbymodule.menu.SettingsMenu;

public class LobbyModuleAction implements ItemActionExecutor {

    @Override
    public void execute(ClickHandler clickHandler, String label, String[] args) {
        try {
            String arg = args[0].toLowerCase();
            Player player = clickHandler.player();
            Language language = PlayerLanguages.get(player.getUniqueId());

            if (arg.equals("settings")) {
                SettingsMenu.get(language).open(clickHandler.player());
            }

            if (arg.equals("lobbies")) {
                LobbiesMenu.get(language).open(player);
            }

            if (arg.equals("minigames")) {
                MinigamesMenu.get(language).open(clickHandler.player());
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
