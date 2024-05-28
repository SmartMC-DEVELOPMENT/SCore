package us.smartmc.lobbymodule.menu;

import me.imsergioh.pluginsapi.handler.LanguagesHandler;
import me.imsergioh.pluginsapi.instance.PlayerLanguages;
import me.imsergioh.pluginsapi.instance.item.ItemBuilder;
import me.imsergioh.pluginsapi.instance.menu.CoreMenu;
import me.imsergioh.pluginsapi.instance.player.CorePlayer;
import me.imsergioh.pluginsapi.language.Language;
import org.bukkit.Material;
import us.smartmc.lobbymodule.handler.FlyManager;
import us.smartmc.lobbymodule.instance.LinkSocialType;
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
        return LanguagesHandler.get(language).get("lobby").getString("title.settingsMenu");
    }

    public SettingsMenu(Language language) {
        super(null, 36, getTitle(language));
        this.language = language;
        menus.put(language, this);
    }

    @Override
    public void load() {
        setFlyingItem();
        //ItemBuilder relleno = ItemBuilder.of(Material.BLACK_STAINED_GLASS_PANE).name(" ");
        //MenuUtil.setBorder(relleno.get(), inventory);

        set(11, ItemBuilder.of(Material.PLAYER_HEAD)
                .data((byte) 3)
                .name("<lang.lobby.items.language.name>")
                .lore(Arrays.asList("<lang.lobby.items.language.description>"))
                .skullTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzY5MTk2YjMzMGM2Yjg5NjJmMjNhZDU2MjdmYjZlY2NlNDcyZWFmNWM5ZDQ0Zjc5MWY2NzA5YzdkMGY0ZGVjZSJ9fX0=")
                .get(language), "cmd lang");

        set(13, ItemBuilder.of(Material.PLAYER_HEAD)
                .data((byte) 3)
                .name("<lang.lobby.items.link_socials.name>")
                .lore(Arrays.asList("<lang.lobby.items.link_socials.description>"))
                .skullTexture(LinkSocialType.DISCORD.getSkullTexture())
                .get(language), "cmd linkSocials");

        set(inventory.getSize() - 5, ItemBuilder.of(Material.BOOK).name("&c<lang.language.menuClose>").get(language), "closeInv");
    }

    public void setFlyingItem() {
        set(15, ItemBuilder.of(Material.FEATHER)
                .name("<lang.lobby.items.flying.name>")
                .lore(Arrays.asList("<lang.lobby.loreFlyToggle>"))
                .get(language), "closeInv", "cmd fly");
    }
}
