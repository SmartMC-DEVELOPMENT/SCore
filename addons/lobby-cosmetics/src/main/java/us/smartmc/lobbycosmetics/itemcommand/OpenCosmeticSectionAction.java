package us.smartmc.lobbycosmetics.itemcommand;

import me.imsergioh.pluginsapi.instance.ItemActionExecutor;
import me.imsergioh.pluginsapi.instance.item.ClickHandler;
import org.bukkit.entity.Player;
import us.smartmc.lobbycosmetics.LobbyCosmetics;
import us.smartmc.lobbycosmetics.handler.CosmeticSectionHandler;
import us.smartmc.lobbycosmetics.instance.cosmetic.CosmeticType;
import us.smartmc.lobbycosmetics.menu.CosmeticSectionMenu;

public class OpenCosmeticSectionAction implements ItemActionExecutor {

    private static final CosmeticSectionHandler sectionsHandler = LobbyCosmetics.getSectionsHandler();

    @Override
    public void execute(ClickHandler handler, String label, String[] args) {
        Player player = handler.player();
        CosmeticType type = CosmeticType.valueOf(args[0]);
        new CosmeticSectionMenu(player, sectionsHandler.get(type)).open(player);
    }
}
