package us.smartmc.event.eventscore.menu;

import me.imsergioh.pluginsapi.instance.item.ItemBuilder;
<<<<<<< Updated upstream
import me.imsergioh.pluginsapi.instance.menu.CoreMenu;
import me.imsergioh.pluginsapi.instance.player.CorePlayer;
import org.bukkit.Material;
import org.bukkit.entity.Player;
=======
import me.imsergioh.pluginsapi.util.ChatUtil;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import us.smartmc.event.eventscore.instance.BroadcastAccessType;
import us.smartmc.event.eventscore.instance.GlobalToggleableItem;
>>>>>>> Stashed changes
import us.smartmc.event.eventscore.types.EventWhitelistType;
import us.smartmc.event.eventscore.util.BroadcastUtil;
import us.smartmc.event.eventscore.util.MenuUtil;

<<<<<<< Updated upstream
=======
import java.util.Arrays;

>>>>>>> Stashed changes
public class MainOptionsMenu extends EventCoreMenu {

    public MainOptionsMenu(Player player) {
        super(player, 27, "Opciones - Evento");
    }

    @Override
    public void load() {
<<<<<<< Updated upstream
        MenuUtil.setColumn(0, ItemBuilder.of(Material.BLACK_STAINED_GLASS_PANE).data(3).name(" ").get(player), this);
        MenuUtil.setColumn(8, ItemBuilder.of(Material.BLACK_STAINED_GLASS_PANE).data(3).name(" ").get(player), this);

        setToggleable(4, EventWhitelistType.class, "whitelist_type");
        set(13, Material.PLAYER_HEAD,
                "&aControl de jugadores",
                "Controla:\n" +
                        "  - Modo de juego\n" +
                        "  - Vida\n" +
                        "  - Teletransportación a X\n" +
                        "  y más!", "openEventControl players");
=======
        MenuUtil.setBorderMenu(ItemBuilder.of(Material.BLACK_STAINED_GLASS_PANE).data(3).name(" ").get(), this);

        set(4, whitelistItem.getItem(), handler -> {
            Player clicker = handler.getPlayer();
            whitelistItem.toggleType();
            set(4, whitelistItem.getItem());
            BroadcastUtil.broadcastMessage(BroadcastAccessType.ADMIN,
                    "&fModo de acceso cambiado a: " + whitelistItem.getItem().getItemMeta().getDisplayName());
        });
        set(13, ItemBuilder.of(Material.REDSTONE)
                .name("&bControl de jugadores").lore(Arrays.asList("Hola ", "esto es un test")).get(), clickHandler -> {
            PlayerControlMenu.getGlobalMenu().open(clickHandler.getPlayer());
        });
>>>>>>> Stashed changes
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
