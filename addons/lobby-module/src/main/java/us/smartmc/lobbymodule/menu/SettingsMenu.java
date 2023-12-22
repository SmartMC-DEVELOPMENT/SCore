package us.smartmc.lobbymodule.menu;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import us.smartmc.core.pluginsapi.spigot.SpigotPluginsAPI;
import us.smartmc.core.pluginsapi.spigot.instance.SpigotAPIConfig;
import us.smartmc.core.pluginsapi.spigot.item.ItemBuilder;
import us.smartmc.core.pluginsapi.spigot.menu.ConfigurableMenu;
import us.smartmc.core.pluginsapi.spigot.player.CorePlayer;
import us.smartmc.core.pluginsapi.util.ChatUtil;
import us.smartmc.lobbymodule.handler.FlyManager;
import us.smartmc.lobbymodule.instance.PlayerVisibility;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;

public class SettingsMenu extends ConfigurableMenu {

    public SettingsMenu(Player player) {
        super(player, new SpigotAPIConfig(new File(SpigotPluginsAPI.getPlugin().getDataFolder() + "/menus", "settings.yml")));
    }

    @Override
    public void load() {
        setVisibilityItem();
        setFlyingItem();
        set(11, ItemBuilder.of(Material.SKULL_ITEM)
                .data((byte) 3)
                .name("<lang.lobby.items_language_name>")
                .lore(Arrays.asList("<lang.lobby.items_language_description>"))
                .skullTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzY5MTk2YjMzMGM2Yjg5NjJmMjNhZDU2MjdmYjZlY2NlNDcyZWFmNWM5ZDQ0Zjc5MWY2NzA5YzdkMGY0ZGVjZSJ9fX0=")
                .get(player), "cmd lang");
    }

    public void setVisibilityItem() {
        set(15, ItemBuilder.of(Material.ENDER_PEARL)
                .name("<lang.lobby.items_visibility_name>")
                .lore(PlayerVisibility.getOptionItemLore(player))
                .get(player), "alternateVisibility");
    }

    public void setFlyingItem() {
        set(13, ItemBuilder.of(Material.FEATHER)
                .name("<lang.lobby.items_flying_name>")
                .lore(Arrays.asList(FlyManager.isFlyingEnabled(CorePlayer.get(player))?"<lang.lobby.lore_fly_enabled>":"<lang.lobby.lore_fly_disabled>"))
                .get(player), "toggleFly");
    }
}
