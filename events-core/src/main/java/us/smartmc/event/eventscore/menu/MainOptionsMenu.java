package us.smartmc.event.eventscore.menu;

import lombok.Getter;
import me.imsergioh.pluginsapi.instance.item.ItemBuilder;
<<<<<<< Updated upstream
import org.bukkit.Material;
import org.bukkit.entity.Player;
import us.smartmc.event.eventscore.instance.GlobalToggleableItem;
=======
import me.imsergioh.pluginsapi.instance.menu.CoreMenu;
import me.imsergioh.pluginsapi.instance.player.CorePlayer;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import us.smartmc.event.eventscore.instance.BroadcastAccessType;
>>>>>>> Stashed changes
import us.smartmc.event.eventscore.types.EventWhitelistType;
import us.smartmc.event.eventscore.util.MenuUtil;

import java.util.Arrays;

public class MainOptionsMenu extends EventCoreMenu {

<<<<<<< Updated upstream
    @Getter
    private static final MainOptionsMenu globalMenu = new MainOptionsMenu();

    private static final GlobalToggleableItem<EventWhitelistType> whitelistItem = new GlobalToggleableItem<>("whitelist_type", EventWhitelistType.class)
            .register(EventWhitelistType.ACCESS_CODE, Material.NAME_TAG, "&eCódigo de acceso")
            .register(EventWhitelistType.PUBLIC, Material.GREEN_DYE, "&aPúblico")
            .register(EventWhitelistType.NAME_LIST, Material.PAPER,"&bLista de nicks");

    private MainOptionsMenu() {
        super(null, 45, "Opciones - Evento");
=======


    public MainOptionsMenu(Player player) {
        super(player, 27, "Opciones - Evento");
>>>>>>> Stashed changes
    }

    @Override
    public void load() {
<<<<<<< Updated upstream
        MenuUtil.setRow(0, ItemBuilder.of(Material.BLACK_STAINED_GLASS_PANE).data(3).name(" ").get(), this);
        MenuUtil.setColumn(8, ItemBuilder.of(Material.BLACK_STAINED_GLASS_PANE).data(3).name(" ").get(), this);

        set(4, whitelistItem.getItem(), handler -> {
            Player player = handler.getPlayer();
=======
        MenuUtil.setColumn(0, ItemBuilder.of(Material.BLACK_STAINED_GLASS_PANE).data(3).name(" ").get(), this);
        MenuUtil.setColumn(8, ItemBuilder.of(Material.BLACK_STAINED_GLASS_PANE).data(3).name(" ").get(), this);

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

        set(4, whitelistItem .getItem(), handler -> {
            Player clicker = handler.getPlayer();
>>>>>>> Stashed changes
            whitelistItem.toggleType();
            set(4, whitelistItem.getItem());

        });
        set(13, ItemBuilder.of(Material.LEGACY_SKULL_ITEM)
                .name("&bControl de jugadores").lore(Arrays.asList(
                        "Cambia aspectos de los jugadores:",
                        "  - Modo de juego")).get(), clickHandler -> {
            PlayerControlMenu.getGlobalMenu().open(clickHandler.getPlayer());
        });
    }
}
