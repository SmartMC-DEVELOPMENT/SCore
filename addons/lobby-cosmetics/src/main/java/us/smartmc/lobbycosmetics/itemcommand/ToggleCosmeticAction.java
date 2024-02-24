package us.smartmc.lobbycosmetics.itemcommand;

import me.imsergioh.pluginsapi.instance.ItemActionExecutor;
import me.imsergioh.pluginsapi.instance.item.ClickHandler;
import org.bukkit.entity.Player;
import us.smartmc.lobbycosmetics.LobbyCosmetics;
import us.smartmc.lobbycosmetics.instance.cosmetic.CosmeticType;
import us.smartmc.lobbycosmetics.instance.cosmetic.ICosmetic;

public class ToggleCosmeticAction implements ItemActionExecutor {

    @Override
    public void execute(ClickHandler handler, String label, String[] args) {
        Player player = handler.player();
        CosmeticType type = CosmeticType.valueOf(args[0]);
        String name = args[1];
        ICosmetic cosmetic = LobbyCosmetics.getSectionsHandler().get(type).get(name);
        player.sendMessage("TOGGLE COSMETIC: " + cosmetic.getId() + "(" + type.name() + ")");
    }
}
