package us.smartmc.lobbymodule.menu;

import me.imsergioh.pluginsapi.handler.LanguagesHandler;
import me.imsergioh.pluginsapi.instance.item.ItemBuilder;
import me.imsergioh.pluginsapi.instance.menu.CoreMenu;
import me.imsergioh.pluginsapi.instance.player.CorePlayer;
import me.imsergioh.pluginsapi.language.Language;
import org.bukkit.Material;
import us.smartmc.lobbymodule.handler.FlyManager;
import us.smartmc.lobbymodule.util.MenuUtil;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class SettingsMenu extends CoreMenu {

    private static final Map<Language, SettingsMenu> menus = new HashMap<>();

    private final Language language;

    public static SettingsMenu get(Language language) {
        if (menus.containsKey(language)) return menus.get(language);
        return new SettingsMenu(language);
    }

    public static String getTitle(Language language) {
        return LanguagesHandler.get(language).get("lobby").getString("title_settings_menu");
    }

    public SettingsMenu(Language language) {
        super(null, 54, getTitle(language));
        this.language = language;
        menus.put(language, this);
    }

    @Override
    public void load() {
        setFlyingItem();
        ItemBuilder relleno = ItemBuilder.of(Material.BLACK_STAINED_GLASS_PANE).name(" ");
        MenuUtil.setBorder(relleno.get(), inventory);


        set(20, ItemBuilder.of(Material.PLAYER_HEAD)
                .data((byte) 3)
                .name("<lang.lobby.items_language_name>")
                .lore(Arrays.asList("<lang.lobby.items_language_description>"))
                .skullTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzY5MTk2YjMzMGM2Yjg5NjJmMjNhZDU2MjdmYjZlY2NlNDcyZWFmNWM5ZDQ0Zjc5MWY2NzA5YzdkMGY0ZGVjZSJ9fX0=")
                .get(language), "cmd lang");

        set(31, ItemBuilder.of(Material.PLAYER_HEAD)
                .data((byte) 3)
                .name("<lang.lobby.items_link_socials_name>")
                .lore(Arrays.asList("<lang.lobby.items_link_socials_description>"))
                .skullTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNmFkNDZhNDIyYWU1OTYwM2ZkODg5YzI1MzQ0ZmY2N2JjODQzYWY4ZWU1MTg5MzJjMmUyYWQwN2NkYmY5MzliMyJ9fX0=")
                .get(language), "cmd linkSocials");

    }

    public void setFlyingItem() {
        set(24, ItemBuilder.of(Material.FEATHER)
                .name("<lang.lobby.items_flying_name>")
                .lore(Arrays.asList("<lang.lobby.lore_fly_toggle>"))
                .get(language), "toggleFly");
    }
}
