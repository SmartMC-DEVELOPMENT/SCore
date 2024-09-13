package us.smartmc.lobbymodule.menu;

import com.google.common.collect.Lists;
import me.imsergioh.pluginsapi.handler.LanguagesHandler;
import me.imsergioh.pluginsapi.instance.item.ItemBuilder;
import me.imsergioh.pluginsapi.instance.menu.CoreMenu;
import me.imsergioh.pluginsapi.instance.player.CorePlayer;
import me.imsergioh.pluginsapi.language.Language;
import org.bson.Document;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import us.smartmc.core.SmartCore;
import us.smartmc.lobbymodule.handler.LobbiesInfoManager;
import us.smartmc.lobbymodule.messages.LobbyMessages;

import java.util.*;


public class LobbiesMenu extends CoreMenu {

    private static final Map<Language, LobbiesMenu> menus = new HashMap<>();

    public static LobbiesMenu get(Language language) {
        if (menus.containsKey(language)) return menus.get(language);
        return new LobbiesMenu(language);
    }

    public static String getTitle(Language language) {
        return LanguagesHandler.get(language).get("lobby").getString("title.lobbiesMenu");
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
        player.playSound(player.getLocation(), Sound.CLICK, 0.1F, 2.5F);
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
        String serverName = SmartCore.getServerId();

        for (Map.Entry<String, Document> entry : LobbiesInfoManager.getInfos().entrySet()) {
            String lobbyId = entry.getKey();
            Document lobbyInfoDocument = entry.getValue();
            boolean isSelf = lobbyId.equals(serverName);
            Material material = Material.QUARTZ_BLOCK;
            byte materialData = 0;
            String variableConnect = "<lang.minigames.clickToConnect>";
            String labelCommand = "connectTo " + lobbyId;
            if (isSelf) {
                material = Material.CLAY;
                materialData = 14;
                variableConnect = "<lang.lobby.already_connected>";
                labelCommand = "closeInv";
            }

            int number = Integer.parseInt(lobbyId.replaceAll("[^0-9]", ""));
            String count = String.valueOf(lobbyInfoDocument.getInteger("online"));

            set(slot, ItemBuilder.of(material).data(materialData).name(getItemName(isSelf), number)
                    .lore(Arrays.asList("&7" + count + "/" + lobbyInfoDocument.getInteger("max"), "&r",
                            variableConnect))
                    .get(language), labelCommand);
            slot++;
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
    }

    public String getItemName(boolean isSelf) {
        String prefixPath = isSelf ? "current_lobby_name_prefix" : "lobby_name_prefix";
        String a = LanguagesHandler.get(language).get(LobbyMessages.NAME).getString(prefixPath);
        String b = LanguagesHandler.get(language).get(LobbyMessages.NAME).getString("main-lobby.name");
        return a + b;
    }

    public static int getDynamicInventorySize() {
        int size = LobbiesInfoManager.lobbyInfoModules().size();

        // Limitar el tamaño a un máximo de 54
        if (size > 54) size = 54;

        // Ajustar el tamaño para que sea un múltiplo de 9
        int remainder = size % 9;
        if (remainder != 0) {
            size += 9 - remainder;
        }

        if (size == 0) {
            size = 9;
        }
        return size;
    }

}