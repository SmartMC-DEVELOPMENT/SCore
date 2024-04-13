package us.smartmc.lobbycosmetics.itemcommand;

import me.imsergioh.pluginsapi.instance.ItemActionExecutor;
import me.imsergioh.pluginsapi.instance.item.ClickHandler;
import us.smartmc.lobbycosmetics.menu.CosmeticBuyMenu;

public class CancelCosmeticPurchase implements ItemActionExecutor {

    @Override
    public void execute(ClickHandler handler, String label, String[] args) {
        CosmeticBuyMenu.cancelBuyRequest(handler.player());
    }
}
