package us.smartmc.lobbycosmetics.itemcommand;

import me.imsergioh.pluginsapi.instance.ItemActionExecutor;
import me.imsergioh.pluginsapi.instance.item.ClickHandler;
import me.imsergioh.pluginsapi.instance.player.CorePlayer;
import org.bukkit.entity.Player;
import us.smartmc.lobbycosmetics.LobbyCosmetics;
import us.smartmc.lobbycosmetics.menu.CosmeticsMainMenu;

public class ItemCosmeticAction implements ItemActionExecutor {

    @Override
    public void execute(ClickHandler handler, String label, String[] args) {
        Player player = handler.player();
        if (!player.hasPermission("*")) {
            CorePlayer.get(player).sendLanguageMessage("lobby", "feature_in_development");
            return;
        }
        new CosmeticsMainMenu(player).open(player);
    }
}
