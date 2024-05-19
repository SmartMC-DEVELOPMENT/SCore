package us.smartmc.lobbymodule.menu;

import me.imsergioh.pluginsapi.handler.LanguagesHandler;
import me.imsergioh.pluginsapi.instance.item.ItemBuilder;
import me.imsergioh.pluginsapi.instance.menu.CoreMenu;
import me.imsergioh.pluginsapi.language.Language;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import us.smartmc.lobbymodule.LobbyModule;
import us.smartmc.lobbymodule.config.MinigamesConfig;

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
    private final int[] slots = {11,12,13,14,15};

    private final Map<String, Integer> fixSlots = new HashMap<>();

    private final Language language;

    private MinigamesMenu(Language language) {
        super(null, 36, LanguagesHandler.get(language).get("minigames").getString("inventoryTitle"));
        this.language = language;
        menus.put(language, this);

        config.getMiniGames().forEach((name, document) -> {
            int slot = document.containsKey("slot") ? document.getInteger("slot") : slots[currentSlotIndex];
            String serverPrefixId = document.getString("serverPrefixId");
            set(slot, MinigamesConfig.getItemOf(language, name), "connectTo " + serverPrefixId, "closeInv");
            fixSlots.put(name, slot);
            currentSlotIndex++;
        });

        setNotAvailableItems();
    }

    @Override
    public void load() {

        if(!fixSlots.isEmpty()){
            config.getMiniGames().forEach((name, document) -> {
                int slot = fixSlots.get(name);
                String serverPrefixId = document.getString("serverPrefixId");
                set(slot, MinigamesConfig.getItemOf(language, name), "connectTo " + serverPrefixId, "closeInv");
            });
        }

        // DISCORD
        set(9, ItemBuilder.of(Material.PLAYER_HEAD)
                .data((byte) 3)
                .name("<lang.minigames.items.discord.name>")
                .lore("<lang.minigames.items.discord.description>")
                .skullTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNGQ0MjMzN2JlMGJkY2EyMTI4MDk3ZjFjNWJiMTEwOWU1YzYzM2MxNzkyNmFmNWZiNmZjMjAwMDAwMTFhZWI1MyJ9fX0=")
                .get(language), "bungeeCMD discord", "closeInv");

        // STORE
        set(18, ItemBuilder.of(Material.PLAYER_HEAD)
                .data((byte) 3)
                .name("<lang.minigames.items.store.name>")
                .lore("<lang.minigames.items.store.description>")
                .skullTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjI3ODQzMDdiODkyZjUyYjkyZjc0ZmE5ZGI0OTg0YzRmMGYwMmViODFjNjc1MmU1ZWJhNjlhZDY3ODU4NDI3ZSJ9fX0=")
                .get(language), "bungeeCMD store", "closeInv");
        closeItem();
    }

    public void closeItem() {
        set(inventory.getSize() - 5, ItemBuilder.of(Material.BOOK).name("&c<lang.language.menuClose>").get(language), "closeInv");
    }

    public void setNotAvailableItems(){
        ItemStack item = ItemBuilder.of(Material.RED_STAINED_GLASS_PANE)
                .name("<lang.lobby.items.notAvailable.name>")
                .lore("<lang.lobby.items.notAvailable.description>")
                .get(language);

        for (int slot : slots) {
            if (get(slot) != null) continue;
            set(slot, item, "msg <lang.lobby.notAvailableName>", "closeInv");
        }
    }
}
