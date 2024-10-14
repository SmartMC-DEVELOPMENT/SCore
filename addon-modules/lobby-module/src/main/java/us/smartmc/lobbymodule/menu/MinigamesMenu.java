package us.smartmc.lobbymodule.menu;

import me.imsergioh.pluginsapi.handler.LanguagesHandler;
import me.imsergioh.pluginsapi.instance.item.ItemBuilder;
import me.imsergioh.pluginsapi.instance.menu.GUIMenu;;
import me.imsergioh.pluginsapi.language.Language;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import us.smartmc.lobbymodule.LobbyModule;
import us.smartmc.lobbymodule.config.MinigamesConfig;

import java.util.HashMap;
import java.util.Map;

public class MinigamesMenu extends GUIMenu {

    private static final Map<Language, MinigamesMenu> menus = new HashMap<>();

    public static MinigamesMenu get(Language language) {
        if (menus.containsKey(language)) return menus.get(language);
        return new MinigamesMenu(language);
    }

    private static final MinigamesConfig config = LobbyModule.getMinigamesConfig();

    private final Language language;

    private MinigamesMenu(Language language) {
        super(null, 36, LanguagesHandler.get(language).get("minigames").getString("inventoryTitle"));
        this.language = language;
        menus.put(language, this);
    }

    @Override
    public void load() {
        config.getMiniGames().forEach((name, document) -> {
            int slot = document.getInteger("slot");
            String serverPrefixId = document.getString("serverPrefixId");
            set(slot, MinigamesConfig.getItemOf(language, name), "connectTo " + serverPrefixId, "closeInv");
        });
        closeItem();
    }

    public void closeItem() {
        set(inventory.getSize() - 5, ItemBuilder.of(Material.BOOK).name("&c<lang.language.menuClose>").get(language), "closeInv");
    }
}
