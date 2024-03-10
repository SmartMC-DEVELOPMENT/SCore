package us.smartmc.lobbymodule.menu;

import me.imsergioh.pluginsapi.handler.LanguagesHandler;
import me.imsergioh.pluginsapi.instance.item.ItemBuilder;
import me.imsergioh.pluginsapi.instance.menu.CoreMenu;
import me.imsergioh.pluginsapi.language.Language;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import us.smartmc.lobbymodule.LobbyModule;
import us.smartmc.lobbymodule.config.MinigamesConfig;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class MinigamesMenu extends CoreMenu {

    private static final Map<Language, MinigamesMenu> menus = new HashMap<>();

    public static MinigamesMenu get(Language language) {
        if (menus.containsKey(language)) return menus.get(language);
        return new MinigamesMenu(language);
    }

    private static final MinigamesConfig config = LobbyModule.getMinigamesConfig();

    private int currentSlotIndex = 0;
    private final int[] slots = {12, 13, 14, 15, 21, 22, 23, 24};

    private final Language language;

    private MinigamesMenu(Language language) {
        super(null, 45, LanguagesHandler.get(language).get("lobby_miniGames").getString("inventory_title"));
        this.language = language;
        menus.put(language, this);
        config.getMiniGames().forEach((name, document) -> {
            int slot = slots[currentSlotIndex];
            String serverPrefixId = document.getString("serverPrefixId");
            set(slot, MinigamesConfig.getItemOf(language, name), "connectTo " + serverPrefixId);
            currentSlotIndex++;
        });
        setNotAvailableItems();
    }

    @Override
    public void load() {
        // DISCORD
        set(9, ItemBuilder.of(Material.SKULL_ITEM)
                .data((byte) 3)
                .name("<lang.lobby_miniGames.items_discord_name>")
                .lore(Arrays.asList("<lang.lobby_miniGames.items_discord_description>"))
                .skullTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNzM5ZWU3MTU0OTc5YjNmODc3MzVhMWM4YWMwODc4MTRiNzkyOGQwNTc2YTI2OTViYTAxZWQ2MTYzMTk0MjA0NSJ9fX0=")
                .get(language), "bungeeCMD discord", "closeInv");
        // TWITTER
        set(18, ItemBuilder.of(Material.SKULL_ITEM)
                .data((byte) 3)
                .name("<lang.lobby_miniGames.items_twitter_name>")
                .lore(Arrays.asList("<lang.lobby_miniGames.items_twitter_description>"))
                .skullTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvY2M3NDVhMDZmNTM3YWVhODA1MDU1NTkxNDllYTE2YmQ0YTg0ZDQ0OTFmMTIyMjY4MThjMzg4MWMwOGU4NjBmYyJ9fX0=")
                .get(language), "bungeeCMD twitter", "closeInv");

        // STORE
        set(27, ItemBuilder.of(Material.SKULL_ITEM)
                .data((byte) 3)
                .name("<lang.lobby_miniGames.items_store_name>")
                .lore(Arrays.asList("<lang.lobby_miniGames.items_store_description>"))
                .skullTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjI3ODQzMDdiODkyZjUyYjkyZjc0ZmE5ZGI0OTg0YzRmMGYwMmViODFjNjc1MmU1ZWJhNjlhZDY3ODU4NDI3ZSJ9fX0=")
                .get(language), "bungeeCMD store", "closeInv");
        closeItem();
    }

    public void closeItem() {
        set(40, ItemBuilder.of(Material.BARRIER).name("&c<lang.language.menu_close>").get(language), "closeInv");
    }

    public void setNotAvailableItems(){
        ItemStack item = ItemBuilder.of(Material.STAINED_GLASS_PANE)
                .data((byte) 14)
                .name("<lang.lobby.items_not_available_name>")
                .lore(Arrays.asList("<lang.lobby.items_not_available_description>"))
                .get(language);

        for (int slot : slots) {
            if (get(slot) != null) continue;
            set(slot, item, "msg <lang.lobby.not_available_message>", "closeInv");
        }
    }
}
