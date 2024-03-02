package us.smartmc.event.eventscore.menu;

import lombok.Getter;
import me.imsergioh.pluginsapi.instance.item.ItemBuilder;
import me.imsergioh.pluginsapi.instance.menu.CoreMenu;
import me.imsergioh.pluginsapi.instance.player.CorePlayer;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import us.smartmc.event.eventscore.EventsCore;
import us.smartmc.event.eventscore.config.EventConfig;
import us.smartmc.event.eventscore.types.EventWhitelistType;
import us.smartmc.event.eventscore.util.MenuUtil;

public class MainOptionsMenu extends EventCoreMenu {

    @Getter
    private static MainOptionsMenu menu;

    private static final EventsCore core = EventsCore.getCore();
    private static final EventConfig config = core.getEventConfig();

    protected MainOptionsMenu(Player player) {
        super(player, 27, "Opciones - Evento");
        if (menu == null) menu = this;
    }

    @Override
    public void load() {
        MenuUtil.setColumn(0, ItemBuilder.of(Material.BLACK_STAINED_GLASS_PANE).data(3).name(" ").get(player), this);
        MenuUtil.setColumn(8, ItemBuilder.of(Material.BLACK_STAINED_GLASS_PANE).data(3).name(" ").get(player), this);

        setToggleable(4, EventWhitelistType.class, "whitelist_type");
        set(13, Material.PLAYER_HEAD, "&aControl de jugadores", """
                Controla:
                - Modo de juego
                - Vida
                - Teletransportación a X
                y más!""", "openEventControl players");
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
