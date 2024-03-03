package us.smartmc.event.eventscore.menu;

import lombok.Getter;
import me.imsergioh.pluginsapi.instance.item.ItemBuilder;
import org.bukkit.Material;
import us.smartmc.event.eventscore.instance.GlobalToggleableItem;
import us.smartmc.event.eventscore.types.PlayerGameModeType;
import us.smartmc.event.eventscore.util.MenuUtil;

public class PlayerControlMenu extends EventCoreMenu {

    @Getter
    private static final PlayerControlMenu globalMenu = new PlayerControlMenu();

    private static final GlobalToggleableItem<PlayerGameModeType> gamemodeItem = new GlobalToggleableItem<>("gamemode", PlayerGameModeType.class)
            .register(PlayerGameModeType.CREATIVE, Material.BEDROCK, "&aCreativo")
            .register(PlayerGameModeType.SURVIVAL, Material.GRASS, "&aSurvival")
            .register(PlayerGameModeType.ADVENTURE, Material.DIAMOND_SWORD, "&aAventura")
            .register(PlayerGameModeType.SPECTATOR, Material.QUARTZ_BLOCK, "&aEspectador");

    private PlayerControlMenu() {
        super(null, 45, "Opciones - Controlar jugadores");
    }

    @Override
    public void load() {
        MenuUtil.setRow(0, ItemBuilder.of(Material.BLACK_STAINED_GLASS_PANE).data(3).name(" ").get(), this);
        MenuUtil.setColumn(8, ItemBuilder.of(Material.BLACK_STAINED_GLASS_PANE).data(3).name(" ").get(), this);

        set(4, gamemodeItem.getItem(), handler -> {
            gamemodeItem.toggleType();
            set(4, gamemodeItem.getItem());
        });
    }
}
