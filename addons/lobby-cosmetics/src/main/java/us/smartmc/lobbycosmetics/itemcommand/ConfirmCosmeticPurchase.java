package us.smartmc.lobbycosmetics.itemcommand;

import me.imsergioh.pluginsapi.instance.ItemActionExecutor;
import me.imsergioh.pluginsapi.instance.item.ClickHandler;
import org.bukkit.entity.Player;
import us.smartmc.lobbycosmetics.LobbyCosmetics;
import us.smartmc.lobbycosmetics.instance.cosmetic.CosmeticType;
import us.smartmc.lobbycosmetics.instance.cosmetic.ICosmetic;
import us.smartmc.lobbycosmetics.instance.player.CosmeticPlayerSession;
import us.smartmc.lobbycosmetics.menu.CosmeticBuyMenu;

public class ConfirmCosmeticPurchase implements ItemActionExecutor {

    @Override
    public void execute(ClickHandler handler, String label, String[] args) {
        CosmeticBuyMenu.acceptBuyRequest(handler.player());
    }
}
