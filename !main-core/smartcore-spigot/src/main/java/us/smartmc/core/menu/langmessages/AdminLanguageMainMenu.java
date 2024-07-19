package us.smartmc.core.menu.langmessages;

import me.imsergioh.pluginsapi.instance.item.ItemBuilder;
import me.imsergioh.pluginsapi.instance.menu.CoreMenu;
import me.imsergioh.pluginsapi.language.Language;
import me.imsergioh.pluginsapi.menu.SetLanguageMenu;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import us.smartmc.core.util.EnumUtil;

import java.util.ArrayList;
import java.util.List;

public class AdminLanguageMainMenu extends CoreMenu {

    private Language selectedLanguage = Language.getDefault();

    public AdminLanguageMainMenu(Player player) {
        super(player, 27, "Admin language");
    }

    @Override
    public void load() {
        setLanguageItem();
    }

    private void setLanguageItem() {
        set(0, getLangItem().get(initPlayer), "adminLanguage toggleLanguage");

    }

    public void toggleSelectedLanguage() {
        selectedLanguage = EnumUtil.getNextEnumValue(selectedLanguage);
        setLanguageItem();
    }

    public ItemBuilder getLangItem() {
        ItemBuilder builder = SetLanguageMenu.getLangItem(initPlayer, selectedLanguage);
        List<String> lore = new ArrayList<>();
        for (Language language : Language.values()) {
            boolean currentLang = language.equals(selectedLanguage);
            String currentName = "<lang.general.lang." + language.name() + ".name>";
            currentName = currentLang ? "&f" + currentName : "&7" + currentName;
            lore.add("&8- " + currentName);
        }
        builder.lore(lore);
        return builder;
    }

    public static String stripColors(String input) {
        // Eliminar los códigos de color con '&'
        String result = input.replaceAll("(?i)&[0-9A-FK-OR]", "");
        // Eliminar los códigos de color con '§'
        result = ChatColor.stripColor(result);
        return result;
    }

}
