package us.smartmc.game.luckytowers.menu;

import me.imsergioh.pluginsapi.instance.item.ItemBuilder;
import me.imsergioh.pluginsapi.instance.menu.CoreMenu;
import me.imsergioh.pluginsapi.language.IMessageCategory;
import org.bukkit.entity.Player;
import us.smartmc.game.luckytowers.messages.GameItems;
import us.smartmc.game.luckytowers.util.MenuUtil;

public abstract class GameMenu extends CoreMenu {

    protected GameMenu(Player player, int size, IMessageCategory title) {
        super(player, size, getTitle(player, title));
    }

    protected GameMenu(Player player, int size, String title) {
        super(player, size, title);
    }

    private static String getTitle(Player player, IMessageCategory title) {
        return MenuUtil.getTitle(player, title);
    }

    protected ItemBuilder item(IMessageCategory category) {
        return ItemBuilder.of(initCorePlayer.getLanguage(), category);
    }

    protected void setLeaveItem(int slot) {
        set(slot, getLeaveItem().get(), "cmd leave");
    }

    private ItemBuilder getLeaveItem() {
        return item(GameItems.spectator_goBack);
    }

}
