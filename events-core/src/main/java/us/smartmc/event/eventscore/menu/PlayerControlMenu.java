package us.smartmc.event.eventscore.menu;

import me.imsergioh.pluginsapi.instance.item.ItemBuilder;
<<<<<<< Updated upstream
import me.imsergioh.pluginsapi.instance.menu.CoreMenu;
import me.imsergioh.pluginsapi.instance.player.CorePlayer;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import us.smartmc.event.eventscore.types.EventWhitelistType;
=======
import me.imsergioh.pluginsapi.util.ChatUtil;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import us.smartmc.event.eventscore.instance.BroadcastAccessType;
import us.smartmc.event.eventscore.instance.GlobalToggleableItem;
import us.smartmc.event.eventscore.types.PlayerGameModeType;
import us.smartmc.event.eventscore.util.BroadcastUtil;
>>>>>>> Stashed changes
import us.smartmc.event.eventscore.util.MenuUtil;

public class PlayerControlMenu extends EventCoreMenu {

<<<<<<< Updated upstream
    protected PlayerControlMenu(Player player) {
        super(player, 27, "Opciones - Controlar jugadores");
=======
    @Getter
    private static final PlayerControlMenu globalMenu = new PlayerControlMenu();

    private static final GlobalToggleableItem<PlayerGameModeType> gamemodeItem = new GlobalToggleableItem<>("gamemode", PlayerGameModeType.class)
            .register(PlayerGameModeType.CREATIVE, Material.BEDROCK, "&bCreativo")
            .register(PlayerGameModeType.SURVIVAL, Material.GRASS, "&cSurvival")
            .register(PlayerGameModeType.ADVENTURE, Material.DIAMOND_SWORD, "&eAventura")
            .register(PlayerGameModeType.SPECTATOR, Material.QUARTZ_BLOCK, "&7Espectador");

    private PlayerControlMenu() {
        super(null, 45, "Opciones - Controlar jugadores");
>>>>>>> Stashed changes
    }

    @Override
    public void load() {
<<<<<<< Updated upstream
        MenuUtil.setColumn(0, ItemBuilder.of(Material.BLACK_STAINED_GLASS_PANE).data(3).name(" ").get(player), this);
        MenuUtil.setColumn(8, ItemBuilder.of(Material.BLACK_STAINED_GLASS_PANE).data(3).name(" ").get(player), this);

        setToggleable(4, EventWhitelistType.class, "whitelist_type");
=======
        MenuUtil.setBorderMenu(ItemBuilder.of(Material.BLACK_STAINED_GLASS_PANE).data(3).name(" ").get(), this);

        set(4, gamemodeItem.getItem(), handler -> {
            gamemodeItem.toggleType();
            set(4, gamemodeItem.getItem());
            BroadcastUtil.broadcastMessage(BroadcastAccessType.ADMIN,
                    "&fModo de juego cambiado a: " + gamemodeItem.getItem().getItemMeta().getDisplayName());
            for (Player player : Bukkit.getOnlinePlayers()) {
                GameMode gameMode = GameMode.valueOf(gamemodeItem.getCurrentType().name());
                player.setGameMode(gameMode);
            }
        });
>>>>>>> Stashed changes
    }

    public static PlayerControlMenu get(Player player) {
        CorePlayer corePlayer = CorePlayer.get(player.getUniqueId());
        CoreMenu menu = corePlayer.getPreviousOpenMenu();
        if (menu instanceof PlayerControlMenu) {
            return (PlayerControlMenu) menu;
        }
        return null;
    }

}
