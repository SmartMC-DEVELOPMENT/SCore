package us.smartmc.core.pluginsapi.menu;

import us.smartmc.core.pluginsapi.instance.PlayerLanguages;
import us.smartmc.core.pluginsapi.language.Language;
import us.smartmc.core.pluginsapi.spigot.item.ItemBuilder;
import us.smartmc.core.pluginsapi.spigot.menu.CoreMenu;
import us.smartmc.core.pluginsapi.spigot.menu.CoreUpdatableMenu;
import us.smartmc.core.pluginsapi.spigot.message.LanguageMessages;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

public class SetLanguageMenu extends CoreMenu {

    private static ItemStack ES_Item;
    private static ItemStack EN_Item;

    public SetLanguageMenu(Player player) {
        super(player, 45, title(player));
    }

    @Override
    public void load() {
        set(8, getCloseMenuItem(player), "closeInv");
        set(21, getES_Item(player), "cmd lang ES");
        set(23, getEN_Item(player), "cmd lang EN");
        set(40, getPreviousMenuItem(player), "openPrevious");
    }

    private ItemStack getLangItem(Player player, Language language, String skulltexture) {
        String name = LanguageMessages.get().get(player, language, "lang_" + language.name() + "_name");
        String material = "SKULL_ITEM";
        String version = Bukkit.getVersion();
        if (version.contains("1.20")) {
            material = "LEGACY_SKULL_ITEM";
        }
        ItemStack item = ItemBuilder.of(Material.getMaterial(material))
                .data(3)
                .name(name)
                .skullTexture(skulltexture).get(player);
        return item;
    }

    private static String title(Player player) {
        return LanguageMessages.get().get(player, PlayerLanguages.get(player.getUniqueId()), "set_lang_title");
    }

    public ItemStack getEN_Item(Player player) {
        if (EN_Item == null) {
            EN_Item = getLangItem(player, Language.EN, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNGNhYzk3NzRkYTEyMTcyNDg1MzJjZTE0N2Y3ODMxZjY3YTEyZmRjY2ExY2YwY2I0YjM4NDhkZTZiYzk0YjQifX19");
        }
        return EN_Item;
    }

    public ItemStack getES_Item(Player player) {
        if (ES_Item == null) {
            ES_Item = getLangItem(player, Language.ES, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzJiZDQ1MjE5ODMzMDllMGFkNzZjMWVlMjk4NzQyODc5NTdlYzNkOTZmOGQ4ODkzMjRkYThjODg3ZTQ4NWVhOCJ9fX0=");
        }
        return ES_Item;
    }
}
