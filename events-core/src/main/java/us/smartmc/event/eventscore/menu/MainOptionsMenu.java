package us.smartmc.event.eventscore.menu;

import me.imsergioh.pluginsapi.instance.item.ItemBuilder;
import me.imsergioh.pluginsapi.instance.menu.CoreMenu;
import me.imsergioh.pluginsapi.instance.player.CorePlayer;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import us.smartmc.event.eventscore.instance.GlobalToggleableItem;
import us.smartmc.event.eventscore.types.EventWhitelistType;
import us.smartmc.event.eventscore.util.MenuUtil;

public class MainOptionsMenu extends EventCoreMenu {

    private static final GlobalToggleableItem<EventWhitelistType> whitelistItem = new GlobalToggleableItem<>("whitelist_type", EventWhitelistType.class)
            .register(EventWhitelistType.ACCESS_CODE, Material.NAME_TAG, "&eCódigo de acceso")
            .register(EventWhitelistType.PUBLIC, Material.GREEN_DYE, "&aPúblico")
            .register(EventWhitelistType.NAME_LIST, Material.PAPER,"&bLista de nicks");

    public MainOptionsMenu(Player player) {
        super(player, 45, "Opciones - Evento");
    }

    @Override
    public void load() {
        MenuUtil.setRow(0, ItemBuilder.of(Material.BLACK_STAINED_GLASS_PANE).data(3).name(" ").get(player), this);
        MenuUtil.setColumn(8, ItemBuilder.of(Material.BLACK_STAINED_GLASS_PANE).data(3).name(" ").get(player), this);

        setToggleableItem(4, whitelistItem);
    }



    public static MainOptionsMenu get(Player player) {
        CorePlayer corePlayer = CorePlayer.get(player.getUniqueId());
        CoreMenu menu = corePlayer.getPreviousOpenMenu();
        if (menu instanceof MainOptionsMenu) {
            return (MainOptionsMenu) menu;
        }
        return null;
    }

}
