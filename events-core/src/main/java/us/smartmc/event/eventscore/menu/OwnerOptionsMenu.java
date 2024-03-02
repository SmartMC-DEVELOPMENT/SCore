package us.smartmc.event.eventscore.menu;

import me.imsergioh.pluginsapi.instance.item.ItemBuilder;
import me.imsergioh.pluginsapi.instance.menu.CoreMenu;
import me.imsergioh.pluginsapi.instance.player.CorePlayer;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import us.smartmc.event.eventscore.util.MenuUtil;

public class OwnerOptionsMenu extends CoreMenu {

    protected OwnerOptionsMenu(Player player) {
        super(player, 27, "Opciones - Evento");
    }

    @Override
    public void load() {
        MenuUtil.setColumn(0, ItemBuilder.of(Material.STAINED_GLASS_PANE).data(3).name(" ").get(player), this);
        MenuUtil.setColumn(8, ItemBuilder.of(Material.STAINED_GLASS_PANE).data(3).name(" ").get(player), this);
    }

    public static OwnerOptionsMenu get(Player player) {
        CorePlayer corePlayer = CorePlayer.get(player.getUniqueId());
        CoreMenu menu = corePlayer.getPreviousOpenMenu();
        if (menu instanceof OwnerOptionsMenu) {
            return (OwnerOptionsMenu) menu;
        }
        return null;
    }

}
