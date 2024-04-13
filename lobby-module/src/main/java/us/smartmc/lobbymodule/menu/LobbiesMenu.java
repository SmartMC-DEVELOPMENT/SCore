package us.smartmc.lobbymodule.menu;

import me.imsergioh.pluginsapi.handler.LanguagesHandler;
import me.imsergioh.pluginsapi.instance.item.ItemBuilder;
import me.imsergioh.pluginsapi.instance.menu.CoreMenu;
import me.imsergioh.pluginsapi.instance.player.CorePlayer;
import me.imsergioh.pluginsapi.language.Language;
import me.imsergioh.pluginsapi.util.ChatUtil;
import me.imsergioh.pluginsapi.util.PaperChatUtil;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import us.smartmc.core.SmartCore;
import us.smartmc.core.variables.CountVariables;
import us.smartmc.lobbymodule.handler.LobbiesInfoManager;
import us.smartmc.lobbymodule.handler.MaxSlotsInfoManager;
import us.smartmc.lobbymodule.messages.LobbyMessages;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;


public class LobbiesMenu extends CoreMenu {

    private static final Map<Language, LobbiesMenu> menus = new HashMap<>();

    public static LobbiesMenu get(Language language){
        if (menus.containsKey(language)) return menus.get(language);
        return new LobbiesMenu(language);
    }

    public static String getTitle(Language language) {
        return LanguagesHandler.get(language).get("lobby").getString("title_lobbies_menu");
    }

    private final Language language;

    private LobbiesMenu(Language language) {
        super(null, getDynamicInventorySize(), getTitle(language));
        this.language = language;
        LobbiesInfoManager.registerMenu(this);
        menus.put(language, this);
    }

    @Override
    public void open(Player player) {
        this.load();
        player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 0.1F, 2.5F);
        CorePlayer.get(player).setCurrentMenuOpen(this);
        player.openInventory(inventory);
    }

    @Override
    public void load() {
        if (size < getDynamicInventorySize()) {
            for (HumanEntity viewer : inventory.getViewers()) {
                if (!(viewer instanceof Player)) continue;
                Player player = (Player) viewer;
                get(language).open(player);
            }
        }

        inventory.clear();
        int slot = 0;
        String serverName = SmartCore.getServerName();
        for (String serverID : CountVariables.getKeysByPrefix(LobbiesInfoManager.getIDPrefix())) {
            boolean isSelf = serverID.equals(serverName);
            Material material = Material.QUARTZ_BLOCK;
            byte materialData = 0;
            String variableConnect = "<lang.lobby_miniGames.click_to_connect>";
            String labelCommand = "connectTo " + serverID;
            if (isSelf) {
                material = Material.CLAY;
                materialData = 14;
                variableConnect = "<lang.lobby.already_connected>";
                labelCommand = "closeInv";
            }

            int number = Integer.parseInt(serverID.replaceAll("[^0-9]", ""));
            String count = CountVariables.getCountOf(serverID);

            set(slot, ItemBuilder.of(material).data(materialData).name(getItemName(isSelf), number)
                    .lore(Arrays.asList("&7" + count + "/" + MaxSlotsInfoManager.getMaxSlotsOf(serverID), "&r",
                            variableConnect))
                    .get(language), labelCommand);
            slot++;
        }
    }

    public String getItemName(boolean isSelf) {
        String prefixPath = isSelf ? "current_lobby_name_prefix" : "lobby_name_prefix";
        String a = LanguagesHandler.get(language).get(LobbyMessages.NAME).getString(prefixPath);
        String b = LanguagesHandler.get(language).get(LobbyMessages.NAME).getString("main_lobby_name");
        return a + b;
    }

    public static int getDynamicInventorySize() {

        int size = CountVariables.getKeysByPrefix(LobbiesInfoManager.getIDPrefix()).size();

        // Limitar el tamaño a un máximo de 54
        if (size > 54) size = 54;

        // Ajustar el tamaño para que sea un múltiplo de 9
        int remainder = size % 9;
        if (remainder != 0) {
            size += 9 - remainder;
        }

        // Asegurar que el tamaño sea al menos 9 si el original es mayor que 0
        if (size == 0 && !CountVariables.getKeysByPrefix(LobbiesInfoManager.getIDPrefix()).isEmpty()) {
            size = 9;
        }

        if (size == 0) {
            size = 9;
        }
        return size;
    }

}