package us.smartmc.lobbymodule.menu;

import me.imsergioh.pluginsapi.SpigotPluginsAPI;
import me.imsergioh.pluginsapi.instance.SpigotYmlConfig;
import me.imsergioh.pluginsapi.instance.item.ItemBuilder;
import me.imsergioh.pluginsapi.instance.menu.ConfigurableMenu;
import me.imsergioh.pluginsapi.instance.player.CorePlayer;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import us.smartmc.lobbymodule.handler.FlyManager;
import us.smartmc.lobbymodule.util.MenuUtil;

import java.io.File;
import java.util.Arrays;

public class SettingsMenu extends ConfigurableMenu {

    public SettingsMenu(Player player) {
        super(player, new SpigotYmlConfig(new File(SpigotPluginsAPI.getPlugin().getDataFolder() + "/menus", "settings.yml")));
    }

    @Override
    public void load() {
        setFlyingItem();
        ItemBuilder relleno = ItemBuilder.of(Material.STAINED_GLASS_PANE).data(3).name(" ");

        MenuUtil.setRow(0, relleno.get(), inventory);

        int lastRow = (inventory.getSize() / 9) - 1;
        MenuUtil.setRow(lastRow, relleno.get(), inventory);

        MenuUtil.setColumn(0, relleno.get(), inventory);
        MenuUtil.setColumn(8, relleno.get(), inventory);

        set(20, ItemBuilder.of(Material.SKULL_ITEM)
                .data((byte) 3)
                .name("<lang.lobby.items_language_name>")
                .lore(Arrays.asList("<lang.lobby.items_language_description>"))
                .skullTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzY5MTk2YjMzMGM2Yjg5NjJmMjNhZDU2MjdmYjZlY2NlNDcyZWFmNWM5ZDQ0Zjc5MWY2NzA5YzdkMGY0ZGVjZSJ9fX0=")
                .get(player), "cmd lang");

        set(31, ItemBuilder.of(Material.SKULL_ITEM)
                .data((byte) 3)
                .name("<lang.lobby.items_link_socials_name>")
                .lore(Arrays.asList("<lang.lobby.items_link_socials_description>"))
                .skullTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNmFkNDZhNDIyYWU1OTYwM2ZkODg5YzI1MzQ0ZmY2N2JjODQzYWY4ZWU1MTg5MzJjMmUyYWQwN2NkYmY5MzliMyJ9fX0=")
                .get(player), "cmd linkSocials");

    }

    public void setFlyingItem() {
        set(24, ItemBuilder.of(Material.FEATHER)
                .name("<lang.lobby.items_flying_name>")
                .lore(Arrays.asList(FlyManager.isFlyingEnabled(CorePlayer.get(player))?"<lang.lobby.lore_fly_enabled>":"<lang.lobby.lore_fly_disabled>"))
                .get(player), "toggleFly");
    }
}
